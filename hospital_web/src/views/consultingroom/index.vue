<template>
  <div class="consultingroom-container">
    <!-- 页面头部 -->
    <div class="medical-card page-header fade-in-up">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-house"></i>
            诊室管理
          </h2>
          <p class="page-description">管理医院各科室的诊室信息，包括诊室配置和设备管理</p>
        </div>
        <div class="header-right">
          <div class="stats-mini">
            <div class="stat-item">
              <span class="stat-number">{{ list.length }}</span>
              <span class="stat-label">总诊室数</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ enabledCount }}</span>
              <span class="stat-label">启用中</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 查询和操作区域 -->
    <div class="medical-card search-card fade-in-up">
      <div class="card-header">
        <h3 class="card-title">
          <i class="el-icon-operation"></i>
          查询与操作
        </h3>
      </div>
      <div class="search-and-action-area">
        <!-- 搜索表单 -->
        <div class="search-form-custom">
          <div class="search-item">
            <label class="search-label">诊室名称</label>
            <el-input
              v-model="searchMap.name"
              placeholder="请输入诊室名称"
              clearable
              class="search-input">
            </el-input>
          </div>

          <div class="search-item">
            <label class="search-label">隶属科室</label>
            <el-select
              v-model="searchMap.departmentId"
              placeholder="请选择隶属科室"
              clearable
              class="search-select">
              <el-option
                v-for="department in departmentList"
                :key="department.id"
                :label="department.name"
                :value="department.id"/>
            </el-select>
          </div>

          <div class="search-item">
            <label class="search-label">诊室状态</label>
            <el-select
              v-model="searchMap.status"
              placeholder="请选择状态"
              clearable
              class="search-select">
              <el-option label="启用" :value="1"></el-option>
              <el-option label="禁用" :value="0"></el-option>
            </el-select>
          </div>

          <div class="search-item">
            <el-button type="primary" @click="fetchData()" class="action-btn">
              <i class="el-icon-search"></i>
              查询
            </el-button>
            <el-button @click="resetSearch()" class="action-btn">
              <i class="el-icon-refresh"></i>
              重置
            </el-button>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button type="primary" @click="handleEdit('')" class="action-btn">
            <i class="el-icon-plus"></i>
            新增诊室
          </el-button>
          <el-button @click="exportData()" class="action-btn">
            <i class="el-icon-download"></i>
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 诊室列表 -->
    <div class="medical-card table-card fade-in-up">
      <div class="card-header">
        <h3 class="card-title">
          <i class="el-icon-menu"></i>
          诊室列表
        </h3>
        <div class="card-extra">
          <el-button type="text" @click="fetchData()" class="refresh-btn">
            <i class="el-icon-refresh"></i>
          </el-button>
        </div>
      </div>
      <el-table
        :data="list"
        class="medical-table"
        v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="诊室名称" width="150" show-overflow-tooltip />
        <el-table-column prop="departmentName" label="隶属科室" width="150" show-overflow-tooltip />
        <el-table-column prop="description" label="诊室描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="created" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.created) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="scope">
            <div class="table-action-buttons">
              <el-button
                size="small"
                type="primary"
                @click="handleEdit(scope.row.id)">
                <i class="el-icon-edit"></i>
                编辑
              </el-button>
              <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.row.id)">
                <i class="el-icon-delete"></i>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="searchMap.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="searchMap.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="count">
        </el-pagination>
      </div>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="pojo" label-width="100px">
        <el-form-item label="诊室名称" required>
          <el-input v-model="pojo.name" placeholder="请输入诊室名称"></el-input>
        </el-form-item>

        <el-form-item label="隶属科室" required>
          <el-select v-model="pojo.departmentId" placeholder="请选择隶属科室" style="width: 100%">
            <el-option
              v-for="department in departmentList"
              :key="department.id"
              :label="department.name"
              :value="department.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="诊室描述">
          <el-input
            v-model="pojo.description"
            type="textarea"
            :rows="4"
            placeholder="请输入诊室描述">
          </el-input>
        </el-form-item>

        <el-form-item label="诊室状态">
          <el-radio-group v-model="pojo.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave()">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import consultingroomApi from '@/api/consultingroom'
import departmentApi from '@/api/department'
import dayjs from 'dayjs'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  data() {
    return {
      // 查询条件
      searchMap: {
        name: '',
        departmentId: null,
        status: null,
        pageNum: 1,
        pageSize: 10
      },
      // 表格数据
      list: [],
      count: 0,
      loading: false,
      // 科室列表
      departmentList: [],
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      // 编辑对象
      pojo: {
        id: null,
        name: '',
        departmentId: null,
        description: '',
        status: 1
      }
    }
  },
  computed: {
    // 启用的诊室数量
    enabledCount() {
      return this.list.filter(item => item.status === 1).length
    }
  },
  created() {
    // 在vue实例创建完后触发钩子函数
    this.fetchData()
    this.getDepartmentList()
  },
  methods: {
    // 查询所有的科室
    getDepartmentList() {
      // 查询所有科室赋值给模型
      departmentApi.findAll().then(response => {
        this.departmentList = response.data
      })
    },

    // 查询数据
    fetchData() {
      consultingroomApi.search(this.searchMap).then(response => {
        this.list = response.data
        this.count = response.count
      }).catch(error => {
        ElMessage.error('查询失败：' + error.message)
      })
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.searchMap.pageSize = val
      this.searchMap.pageNum = 1
      this.fetchData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.searchMap.pageNum = val
      this.fetchData()
    },

    // 编辑
    handleEdit(id) {
      this.dialogVisible = true
      if (id === '' || id === null) {
        this.dialogTitle = '新增诊室'
        this.pojo = {
          id: null,
          name: '',
          departmentId: null,
          description: '',
          status: 1
        }
      } else {
        this.dialogTitle = '编辑诊室'
        consultingroomApi.findById(id).then(response => {
          this.pojo = response.data
        }).catch(error => {
          ElMessage.error('查询失败：' + error.message)
        })
      }
    },

    // 保存
    handleSave() {
      if (!this.pojo.name) {
        ElMessage.warning('请输入诊室名称')
        return
      }
      if (!this.pojo.departmentId) {
        ElMessage.warning('请选择隶属科室')
        return
      }

      consultingroomApi.saveOrUpdate(this.pojo.id, this.pojo).then(response => {
        if (response.code === 0) {
          ElMessage.success('保存成功')
          this.dialogVisible = false
          this.fetchData()
        } else {
          ElMessage.error('保存失败：' + response.msg)
        }
      }).catch(error => {
        ElMessage.error('保存失败：' + error.message)
      })
    },

    // 删除
    handleDelete(id) {
      ElMessageBox.confirm('确定要删除这个诊室吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        consultingroomApi.deleteById(id).then(response => {
          if (response.code === 0) {
            ElMessage.success('删除成功')
            this.fetchData()
          } else {
            ElMessage.error('删除失败：' + response.msg)
          }
        }).catch(error => {
          ElMessage.error('删除失败：' + error.message)
        })
      }).catch(() => {
        ElMessage.info('已取消删除')
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
    },

    // 重置搜索
    resetSearch() {
      this.searchMap = {
        name: '',
        departmentId: null,
        status: null,
        pageNum: 1,
        pageSize: 10
      }
      this.fetchData()
    },

    // 导出数据
    exportData() {
      ElMessage.info('导出功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/medical-theme.scss';

.consultingroom-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

/* 页面头部 */
.page-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #262626;
  display: flex;
  align-items: center;

  i {
    margin-right: 12px;
    color: #1890ff;
    font-size: 28px;
  }
}

.page-description {
  margin: 0;
  color: #8c8c8c;
  font-size: 14px;
}

.header-right {
  display: flex;
  align-items: center;
}

.stats-mini {
  display: flex;
  gap: 24px;
}

.stat-item {
  text-align: center;

  .stat-number {
    display: block;
    font-size: 24px;
    font-weight: 600;
    color: #1890ff;
    line-height: 1;
  }

  .stat-label {
    display: block;
    font-size: 12px;
    color: #8c8c8c;
    margin-top: 4px;
  }
}

/* 搜索和操作区域 */
.search-card {
  margin-bottom: 24px;
}

.search-and-action-area {
  padding: 24px;
}

/* 自定义搜索表单 */
.search-form-custom {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-label {
  color: #606266;
  font-weight: 500;
  font-size: 14px;
  white-space: nowrap;
  margin-right: 4px;
}

.search-input {
  width: 200px;

  :deep(.el-input__wrapper) {
    border-radius: 6px;
    border: 1px solid #dcdfe6;
    transition: all 0.3s ease;
    box-shadow: none;

    &:hover {
      border-color: #c0c4cc;
    }

    &.is-focus {
      border-color: #1890ff;
      box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
    }
  }
}

.search-select {
  width: 150px;

  :deep(.el-input__wrapper) {
    border-radius: 6px;
    border: 1px solid #dcdfe6;
    transition: all 0.3s ease;
    box-shadow: none;

    &:hover {
      border-color: #c0c4cc;
    }

    &.is-focus {
      border-color: #1890ff;
      box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
    }
  }
}

.action-btn {
  border-radius: 6px;
  padding: 8px 16px;
  font-weight: 500;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  }
}

.action-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
}

.card-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #262626;
  display: flex;
  align-items: center;
}

.card-title i {
  margin-right: 8px;
  color: #1890ff;
}

.card-extra .refresh-btn {
  color: #1890ff;
  transition: all 0.3s ease;
}

.card-extra .refresh-btn:hover {
  color: #40a9ff;
  transform: rotate(180deg);
}

/* 表格样式 */
.table-card {
  margin-bottom: 24px;
}

.medical-table {
  :deep(.el-table) {
    background: #ffffff;
    border: none;

    th {
      background: #ffffff !important;
      background-image: none !important;
      background-color: #ffffff !important;
      color: #262626 !important;
      font-weight: 600;
      border-bottom: 1px solid #ebeef5 !important;
      border-right: none;
    }

    th.el-table__cell {
      background: #ffffff !important;
      background-image: none !important;
      background-color: #ffffff !important;
    }

    th .cell {
      color: #262626 !important;
      font-weight: 600;
    }

    td {
      border-bottom: 1px solid #ebeef5;
      border-right: none;
      background: #ffffff;
    }

    td .cell {
      color: #606266;
    }

    tr:hover {
      background-color: #f5f7fa !important;

      td {
        background-color: #f5f7fa !important;
      }
    }

    .el-table__header-wrapper {
      background: #ffffff !important;
      background-image: none !important;
    }

    .el-table__header {
      background: #ffffff !important;
      background-image: none !important;
    }

    .el-table__header th {
      background: #ffffff !important;
      background-image: none !important;
      background-color: #ffffff !important;
    }

    .el-table__body-wrapper {
      background: #ffffff;
    }
  }
}

.table-action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.table-action-buttons .el-button {
  border-radius: 4px;
  font-size: 12px;
  padding: 4px 8px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  }

  i {
    margin-right: 4px;
    font-size: 12px;
  }
}

/* 分页样式 */
.pagination-container {
  padding: 16px 24px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #ebeef5;
  background: #ffffff;
}

/* 动画效果 */
.fade-in-up {
  animation: fadeInUp 0.6s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .consultingroom-container {
    padding: 12px;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .search-and-action-area {
    padding: 16px;
  }

  .search-form-custom {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
    margin-bottom: 16px;
    padding-bottom: 16px;
  }

  .search-item {
    width: 100%;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .search-input {
    width: 100%;
  }

  .search-select {
    width: 100%;
  }

  .action-buttons {
    flex-direction: column;
    gap: 8px;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }

  .table-action-buttons {
    flex-direction: column;
    gap: 4px;
  }

  .table-action-buttons .el-button {
    width: 100%;
    margin: 0;
  }
}
</style>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
