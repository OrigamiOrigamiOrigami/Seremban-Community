<template>
  <div class="permission-container">
    <!-- 页面头部 -->
    <div class="medical-card page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-key"></i>
            权限管理
          </h2>
          <p class="page-description">管理系统权限和权限标识</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            新增权限
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="medical-card search-section">
      <div class="search-form">
        <div class="search-item">
          <label>权限名称：</label>
          <el-input
            v-model="searchForm.permissionName"
            placeholder="请输入权限名称"
            clearable
            style="width: 200px"
          />
        </div>
        <div class="search-item">
          <label>权限标识：</label>
          <el-input
            v-model="searchForm.permissionCode"
            placeholder="请输入权限标识"
            clearable
            style="width: 200px"
          />
        </div>
        <div class="search-item">
          <label>权限描述：</label>
          <el-input
            v-model="searchForm.description"
            placeholder="请输入权限描述"
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
          <el-button type="warning" icon="el-icon-upload2" @click="showImportDialog">
            导入
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
        <el-table-column label="权限名称" min-width="120">
          <template #default="scope">
            {{ scope.row.permissionName || scope.row.permission_name }}
          </template>
        </el-table-column>
        <el-table-column label="权限标识" min-width="150">
          <template #default="scope">
            <el-tag type="primary" size="small">{{ scope.row.permissionCode || scope.row.permission_code }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="权限描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="关联角色" width="100" align="center">
          <template #default="scope">
            <el-tag type="success" size="small">{{ scope.row.roleCount || scope.row.role_count || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="created" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.created) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">
              编辑
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
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="form.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permissionCode">
          <el-input v-model="form.permissionCode" placeholder="请输入权限标识，如：admin:manage" />
          <div class="form-tip">
            权限标识格式：模块:操作，如 admin:manage、role:view 等
          </div>
        </el-form-item>
        <el-form-item label="权限描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入权限描述"
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

    <!-- 导入对话框 -->
    <el-dialog
      title="导入权限数据"
      v-model="importDialogVisible"
      width="500px"
    >
      <div class="import-content">
        <div class="import-tips">
          <el-alert
            title="导入说明"
            type="info"
            :closable="false"
            show-icon
          >
            <template #default>
              <p>1. 请先下载模板文件，按照模板格式填写数据</p>
              <p>2. 支持的文件格式：.xlsx</p>
              <p>3. 权限标识必须唯一，重复的数据将被忽略</p>
            </template>
          </el-alert>
        </div>

        <div class="import-actions">
          <el-button type="primary" @click="downloadTemplate">
            <i class="el-icon-download"></i>
            下载模板
          </el-button>
        </div>

        <div class="import-upload">
          <el-upload
            ref="uploadRef"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="false"
            accept=".xlsx"
          >
            <el-button type="success">
              <i class="el-icon-upload2"></i>
              选择文件上传
            </el-button>
          </el-upload>
        </div>
      </div>

      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import permissionApi from '@/api/permission'

export default {
  name: 'PermissionManagement',
  data() {
    return {
      // 搜索表单
      searchForm: {
        permissionName: '',
        permissionCode: '',
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
      dialogTitle: '新增权限',
      saveLoading: false,
      // 导入对话框
      importDialogVisible: false,
      uploadUrl: process.env.VUE_APP_BASE_API + '/excel/import/permission',
      uploadHeaders: {},
      // 表单
      form: {
        id: null,
        permissionName: '',
        permissionCode: '',
        description: ''
      },
      // 表单验证规则
      rules: {
        permissionName: [
          { required: true, message: '请输入权限名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        permissionCode: [
          { required: true, message: '请输入权限标识', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9:_-]+$/, message: '权限标识格式不正确，只能包含字母、数字、冒号、下划线和横线', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadData()
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
        const response = await permissionApi.search(searchMap)
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

    // 搜索
    handleSearch() {
      this.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        permissionName: '',
        permissionCode: '',
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
        const response = await request.post('/excel/export/permission', this.searchForm, {
          responseType: 'blob'
        })

        // 创建下载链接
        const blob = new Blob([response.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '权限数据.xlsx'
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

    // 显示导入对话框
    showImportDialog() {
      this.importDialogVisible = true
      // 设置上传头部（包含token）
      this.uploadHeaders = {
        'Authorization': 'Bearer ' + this.$store.getters.token
      }
    },

    // 下载模板
    async downloadTemplate() {
      try {
        // 使用统一的request工具，支持blob响应类型
        const request = require('@/utils/request').default
        const response = await request.get('/excel/template/permission', {
          responseType: 'blob'
        })

        // 创建下载链接
        const blob = new Blob([response.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '权限导入模板.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('模板下载成功')
      } catch (error) {
        console.error('模板下载失败:', error)
        this.$message.error('模板下载失败：' + (error.response?.data?.msg || error.message || '请重试'))
      }
    },

    // 上传前检查
    beforeUpload(file) {
      const isXlsx = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isXlsx) {
        this.$message.error('只能上传 .xlsx 格式的文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    // 上传成功
    handleUploadSuccess(response) {
      this.$message.success('导入成功')
      this.importDialogVisible = false
      this.loadData() // 重新加载数据
    },

    // 上传失败
    handleUploadError(error) {
      this.$message.error('导入失败：' + error.message)
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
      this.dialogTitle = '新增权限'
      this.form = {
        id: null,
        permissionName: '',
        permissionCode: '',
        description: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑权限'
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该权限吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await permissionApi.delete([row.id])
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
        this.$message.warning('请选择要删除的权限')
        return
      }

      this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个权限吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(row => row.id)
          const response = await permissionApi.delete(ids)
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
          response = await permissionApi.update(this.form)
        } else {
          response = await permissionApi.save(this.form)
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

    // 关闭对话框
    handleDialogClose() {
      this.dialogVisible = false
      this.$refs.formRef?.resetFields()
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
.permission-container {
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

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .permission-container {
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
