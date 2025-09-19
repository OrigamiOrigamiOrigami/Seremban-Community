<template>
  <div class="role-container">
    <!-- 页面头部 -->
    <div class="medical-card page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user-solid"></i>
            角色管理
          </h2>
          <p class="page-description">管理系统角色和权限分配</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            新增角色
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="medical-card search-section">
      <div class="search-form">
        <div class="search-item">
          <label>角色名称：</label>
          <el-input
            v-model="searchForm.roleName"
            placeholder="请输入角色名称"
            clearable
            style="width: 200px"
          />
        </div>
        <div class="search-item">
          <label>角色描述：</label>
          <el-input
            v-model="searchForm.description"
            placeholder="请输入角色描述"
            clearable
            style="width: 200px"
          />
        </div>
        <div class="search-buttons">
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
          <el-button type="success" icon="el-icon-download" @click="exportData">
            导出
          </el-button>
        </div>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="medical-card table-section">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="角色名称" min-width="120">
          <template #default="scope">
            {{ scope.row.roleName || scope.row.role_name }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="角色描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="用户数量" width="100" align="center">
          <template #default="scope">
            <el-tag type="info" size="small">{{ scope.row.adminCount || scope.row.admin_count || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="created" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.created) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button size="small" type="success" @click="handleAssignPermissions(scope.row)">
              分配权限
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>

      <!-- 批量操作 -->
      <div class="batch-operations" v-if="selectedRows.length > 0">
        <el-button type="danger" @click="handleBatchDelete">
          批量删除 ({{ selectedRows.length }})
        </el-button>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="saveLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog
      title="分配权限"
      v-model="permissionDialogVisible"
      width="800px"
      :before-close="handlePermissionDialogClose"
    >
      <div class="permission-assignment">
        <div class="role-info">
          <h4>为角色 "{{ currentRole.roleName }}" 分配权限</h4>
          <p>{{ currentRole.description }}</p>
        </div>
        <el-tree
          :data="permissionTree"
          :props="treeProps"
          show-checkbox
          node-key="id"
          ref="permissionTreeRef"
          :default-checked-keys="checkedPermissions"
          class="permission-tree"
        />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handlePermissionDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSavePermissions" :loading="permissionSaveLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import roleApi from '@/api/role'
import permissionApi from '@/api/permission'

export default {
  name: 'RoleManagement',
  data() {
    return {
      // 搜索表单
      searchForm: {
        roleName: '',
        description: ''
      },
      // 表格数据
      tableData: [],
      loading: false,
      // 分页
      currentPage: 1,
      pageSize: 10,
      total: 0,
      // 选中的行
      selectedRows: [],
      // 对话框
      dialogVisible: false,
      dialogTitle: '新增角色',
      saveLoading: false,
      // 表单
      form: {
        id: null,
        roleName: '',
        description: ''
      },
      // 表单验证规则
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      },
      // 权限分配
      permissionDialogVisible: false,
      permissionSaveLoading: false,
      currentRole: {},
      permissionTree: [],
      checkedPermissions: [],
      treeProps: {
        children: 'children',
        label: 'permissionName'
      }
    }
  },
  created() {
    this.loadData()
    this.loadPermissions()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const searchMap = {
          ...this.searchForm,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
        const response = await roleApi.search(searchMap)
        if (response.code === 0) {
          // 后端返回的数据结构：{ code: 0, data: [...], count: 10 }
          this.tableData = response.data || []
          this.total = response.count || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载权限数据
    async loadPermissions() {
      try {
        const response = await permissionApi.findAll()
        if (response.data.code === 0) {
          this.permissionTree = response.data.data
        }
      } catch (error) {
        console.error('加载权限数据失败:', error)
      }
    },

    // 搜索
    handleSearch() {
      this.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        roleName: '',
        description: ''
      }
      this.currentPage = 1
      this.loadData()
    },

    // 导出数据
    async exportData() {
      try {
        // 使用统一的request工具，支持blob响应类型
        const request = require('@/utils/request').default
        const response = await request.post('/excel/export/role', this.searchForm, {
          responseType: 'blob'
        })

        // 创建下载链接
        const blob = new Blob([response.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '角色数据.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败：' + (error.response?.data?.msg || error.message || '请重试'))
      }
    },

    // 分页
    handleSizeChange(val) {
      this.pageSize = val
      this.loadData()
    },

    handleCurrentChange(val) {
      this.currentPage = val
      this.loadData()
    },

    // 选择行
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增角色'
      this.form = {
        id: null,
        roleName: '',
        description: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await roleApi.delete([row.id])
          if (response.data.code === 0) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.data.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的角色')
        return
      }

      this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个角色吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(row => row.id)
          const response = await roleApi.delete(ids)
          if (response.data.code === 0) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.data.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 保存
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true

        let response
        if (this.form.id) {
          response = await roleApi.update(this.form)
        } else {
          response = await roleApi.save(this.form)
        }

        if (response.data.code === 0) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.data.msg || '保存失败')
        }
      } catch (error) {
        if (error !== false) { // 不是表单验证错误
          this.$message.error('保存失败')
        }
      } finally {
        this.saveLoading = false
      }
    },

    // 分配权限
    async handleAssignPermissions(row) {
      this.currentRole = row
      this.permissionDialogVisible = true

      // 加载角色已有的权限
      try {
        const response = await roleApi.getRolePermissions(row.id)
        if (response.data.code === 0) {
          this.checkedPermissions = response.data.data
        }
      } catch (error) {
        console.error('加载角色权限失败:', error)
      }
    },

    // 保存权限分配
    async handleSavePermissions() {
      try {
        this.permissionSaveLoading = true
        const checkedKeys = this.$refs.permissionTreeRef.getCheckedKeys()
        const response = await roleApi.assignPermissions(this.currentRole.id, checkedKeys)

        if (response.data.code === 0) {
          this.$message.success('权限分配成功')
          this.permissionDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.data.msg || '权限分配失败')
        }
      } catch (error) {
        this.$message.error('权限分配失败')
      } finally {
        this.permissionSaveLoading = false
      }
    },

    // 关闭对话框
    handleDialogClose() {
      this.dialogVisible = false
      this.$refs.formRef?.resetFields()
    },

    handlePermissionDialogClose() {
      this.permissionDialogVisible = false
      this.currentRole = {}
      this.checkedPermissions = []
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.role-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.medical-card {
  background: #ffffff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title i {
  color: #409eff;
  font-size: 28px;
}

.page-description {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-section {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-item label {
  white-space: nowrap;
  color: #606266;
  font-weight: 500;
}

.search-buttons {
  display: flex;
  gap: 12px;
}

.table-section {
  position: relative;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.batch-operations {
  position: absolute;
  bottom: 20px;
  left: 20px;
}

.permission-assignment {
  max-height: 500px;
  overflow-y: auto;
}

.role-info {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.role-info h4 {
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.role-info p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.permission-tree {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  max-height: 300px;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .role-container {
    padding: 10px;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .search-form {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-item {
    width: 100%;
  }

  .search-buttons {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>
