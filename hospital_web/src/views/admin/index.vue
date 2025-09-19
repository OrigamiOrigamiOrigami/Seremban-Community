<template>
  <div class="admin-container">
    <!-- 页面头部 -->
    <div class="page-header fade-in-up">
      <h1 class="header-title">
        <i class="el-icon-user-solid header-icon"></i>
        管理员管理
      </h1>
      <p class="header-subtitle">管理系统管理员账号，包括添加、编辑、删除等操作</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="24" class="stats-row fade-in-up">
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <div class="stats-card">
          <div class="stats-content">
            <h3 class="stats-number">{{ totalCount }}</h3>
            <p class="stats-label">总管理员数</p>
          </div>
          <i class="el-icon-user stats-icon"></i>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <div class="stats-card success">
          <div class="stats-content">
            <h3 class="stats-number">{{ onlineCount }}</h3>
            <p class="stats-label">在线管理员</p>
          </div>
          <i class="el-icon-success stats-icon"></i>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <div class="stats-card warning">
          <div class="stats-content">
            <h3 class="stats-number">{{ selectedIds.length }}</h3>
            <p class="stats-label">已选择</p>
          </div>
          <i class="el-icon-check stats-icon"></i>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <div class="stats-card danger">
          <div class="stats-content">
            <h3 class="stats-number">0</h3>
            <p class="stats-label">待审核</p>
          </div>
          <i class="el-icon-warning stats-icon"></i>
        </div>
      </el-col>
    </el-row>

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
            <label class="search-label">姓名</label>
            <el-input
              v-model="searchMap.name"
              placeholder="请输入姓名"
              clearable
              class="search-input">
            </el-input>
          </div>

          <div class="search-item">
            <label class="search-label">手机号码</label>
            <el-input
              v-model="searchMap.phone"
              placeholder="请输入手机号码"
              clearable
              class="search-input">
            </el-input>
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
            新增管理员
          </el-button>
          <el-button
            type="danger"
            @click="handleBatchDelete()"
            :disabled="selectedIds.length === 0"
            class="action-btn">
            <i class="el-icon-delete"></i>
            批量删除 ({{ selectedIds.length }})
          </el-button>
          <el-button @click="exportData()" class="action-btn">
            <i class="el-icon-download"></i>
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="medical-card table-card fade-in-up">
      <div class="card-header">
        <h3 class="card-title">
          <i class="el-icon-menu"></i>
          管理员列表
        </h3>
        <div class="card-extra">
          <el-tooltip content="刷新数据" placement="top">
            <el-button
              type="text"
              icon="el-icon-refresh"
              @click="fetchData()"
              class="refresh-btn">
            </el-button>
          </el-tooltip>
        </div>
      </div>

      <div class="medical-table">
        <el-table
          :data="list"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          v-loading="loading"
          element-loading-text="加载中..."
          element-loading-spinner="el-icon-loading">
          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="id" label="ID" width="80" align="center">
            <template #default="scope">
              <el-tag size="mini" type="info">#{{ scope.row.id }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="image" label="头像" width="80" align="center">
            <template #default="scope">
              <div class="avatar-cell">
                <el-avatar
                  v-if="scope.row.image"
                  :src="scope.row.image"
                  :size="40"
                  class="user-avatar">
                </el-avatar>
                <el-avatar v-else :size="40" class="user-avatar">
                  <i class="el-icon-user-solid"></i>
                </el-avatar>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="120" show-overflow-tooltip>
            <template #default="scope">
              <div class="name-cell">
                <span class="user-name">{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="用户名" width="120" show-overflow-tooltip>
            <template #default="scope">
              <div class="user-info">
                <i class="el-icon-user"></i>
                <span>{{ scope.row.username }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号码" width="150" show-overflow-tooltip>
            <template #default="scope">
              <div class="contact-info">
                <i class="el-icon-phone"></i>
                <span>{{ scope.row.phone }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" width="200" show-overflow-tooltip>
            <template #default="scope">
              <div class="contact-info">
                <i class="el-icon-message"></i>
                <span>{{ scope.row.email }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="roleNames" label="角色" width="180" show-overflow-tooltip>
            <template #default="scope">
              <div class="role-info">
                <i class="el-icon-user-solid"></i>
                <span v-if="scope.row.roleNames" class="role-names">{{ scope.row.roleNames }}</span>
                <el-tag v-else type="info" size="mini">未分配角色</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="created" label="创建时间" width="180" show-overflow-tooltip>
            <template #default="scope">
              <div class="time-info">
                <i class="el-icon-time"></i>
                <span>{{ formatDate(scope.row.created) }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center" fixed="right">
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
      </div>
    </div>

    <!-- 分页组件 -->
    <div class="medical-card pagination-card fade-in-up">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="searchMap.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="searchMap.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="count"
        background
        class="medical-pagination">
      </el-pagination>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="pojo" label-width="100px">
        <el-form-item label="头像">
          <div class="avatar-uploader">
            <el-upload
              class="avatar-uploader-el"
              :action="uploadAction"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :on-error="handleAvatarError"
              :before-upload="beforeAvatarUpload">
              <img v-if="pojo.image" :src="pojo.image" class="avatar">
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="姓名" required>
          <el-input v-model="pojo.name" placeholder="请输入姓名"></el-input>
        </el-form-item>

        <el-form-item label="用户名" required>
          <el-input v-model="pojo.username" placeholder="请输入用户名"></el-input>
        </el-form-item>

        <!-- 新增时不需要密码字段，后端会自动生成 -->
        <el-form-item v-if="!pojo.id" label="说明">
          <el-alert
            title="密码说明"
            description="系统将自动使用手机号后6位作为初始密码，并发送邮件通知"
            type="info"
            :closable="false">
          </el-alert>
        </el-form-item>

        <el-form-item label="手机号码" required>
          <el-input v-model="pojo.phone" placeholder="请输入手机号码"></el-input>
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="pojo.email" placeholder="请输入邮箱"></el-input>
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
import adminApi from '@/api/admin'
import dayjs from 'dayjs'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

export default {
  components: {
    Plus
  },
  data() {
    return {
      // 查询条件
      searchMap: {
        name: '',
        phone: '',
        pageNum: 1,
        pageSize: 10
      },
      // 表格数据
      list: [],
      count: 0,
      loading: false,
      // 选中的ID
      selectedIds: [],
      // 统计数据
      totalCount: 0,
      onlineCount: 0,
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      // 编辑对象
      pojo: {
        id: null,
        name: '',
        username: '',
        phone: '',
        email: '',
        image: ''
      },
      // 上传地址
      uploadAction: process.env.VUE_APP_BASE_API + '/upload/avatar'
    }
  },
  computed: {
    // 上传请求头，包含token
    uploadHeaders() {
      const token = this.$store.getters.token
      if (token) {
        return {
          'Authorization': 'Bearer ' + token,
          'token': token  // 有些后端可能期望这种格式
        }
      }
      return {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    // 查询数据
    fetchData() {
      this.loading = true
      adminApi.search(this.searchMap).then(response => {
        this.list = response.data
        this.count = response.count
        this.totalCount = response.count
        // 模拟在线用户数
        this.onlineCount = Math.floor(Math.random() * this.totalCount) + 1
      }).catch(error => {
        ElMessage.error('查询失败：' + error.message)
      }).finally(() => {
        this.loading = false
      })
    },

    // 重置搜索
    resetSearch() {
      this.searchMap = {
        name: '',
        phone: '',
        pageNum: 1,
        pageSize: 10
      }
      this.fetchData()
    },

    // 导出数据
    async exportData() {
      try {
        // 使用统一的request工具，支持blob响应类型
        const request = require('@/utils/request').default
        const response = await request.post('/excel/export/admin', this.searchMap, {
          responseType: 'blob'
        })

        // 创建下载链接
        const blob = new Blob([response.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '管理员数据.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        ElMessage.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        ElMessage.error('导出失败：' + (error.response?.data?.msg || error.message || '请重试'))
      }
    },

    // 更新当前用户信息到导航栏
    updateCurrentUserInfo() {
      try {
        const { getUserInfoFromToken } = require('@/utils/token')
        const currentUserInfo = getUserInfoFromToken(this.$store.getters.token)

        if (currentUserInfo && (this.pojo.id === currentUserInfo.id || this.pojo.username === currentUserInfo.username)) {
          // 是当前登录用户，更新导航栏信息
          this.$store.commit('user/SET_NAME', this.pojo.name || this.pojo.username)
          this.$store.commit('user/SET_AVATAR', this.pojo.image || '')
          console.log('已更新导航栏用户信息')

          // 如果用户名发生了变化，提示用户重新登录以获取新的token
          if (this.pojo.username !== currentUserInfo.username) {
            this.$confirm('用户名已修改，需要重新登录以更新系统信息，是否立即重新登录？', '提示', {
              confirmButtonText: '重新登录',
              cancelButtonText: '稍后再说',
              type: 'warning'
            }).then(() => {
              this.$store.dispatch('user/logout').then(() => {
                this.$router.push('/login')
              })
            }).catch(() => {
              console.log('用户选择稍后重新登录')
            })
          }
        }
      } catch (error) {
        console.error('更新用户信息失败:', error)
      }
    },

    // 查看详情
    handleView(row) {
      ElMessageBox.alert(`
        <div style="text-align: left;">
          <p><strong>ID:</strong> ${row.id}</p>
          <p><strong>姓名:</strong> ${row.name}</p>
          <p><strong>用户名:</strong> ${row.username}</p>
          <p><strong>手机:</strong> ${row.phone}</p>
          <p><strong>邮箱:</strong> ${row.email}</p>
          <p><strong>创建时间:</strong> ${this.formatDate(row.created)}</p>
        </div>
      `, '管理员详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },

    // 获取状态类型
    getStatusType(row) {
      // 模拟状态逻辑
      return Math.random() > 0.5 ? 'success' : 'info'
    },

    // 获取状态文本
    getStatusText(row) {
      // 模拟状态逻辑
      return Math.random() > 0.5 ? '在线' : '离线'
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

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.id)
    },

    // 编辑
    handleEdit(id) {
      this.dialogVisible = true
      if (id === '' || id === null) {
        this.dialogTitle = '新增管理员'
        this.pojo = {
          id: null,
          name: '',
          username: '',
          phone: '',
          email: '',
          image: ''
        }
      } else {
        this.dialogTitle = '编辑管理员'
        adminApi.findById(id).then(response => {
          console.log('编辑管理员数据:', response.data)
          this.pojo = response.data
        }).catch(error => {
          ElMessage.error('查询失败：' + error.message)
        })
      }
    },

    // 保存
    handleSave() {
      if (!this.pojo.name) {
        ElMessage.warning('请输入姓名')
        return
      }
      if (!this.pojo.username) {
        ElMessage.warning('请输入用户名')
        return
      }
      if (!this.pojo.phone) {
        ElMessage.warning('请输入手机号码')
        return
      }
      if (!this.pojo.email) {
        ElMessage.warning('请输入邮箱')
        return
      }
      if (!this.pojo.image) {
        ElMessage.warning('请上传头像')
        return
      }

      // 验证手机号格式
      const phoneRegex = /^1[3-9]\d{9}$/
      if (!phoneRegex.test(this.pojo.phone)) {
        ElMessage.warning('手机号格式不正确')
        return
      }

      // 验证邮箱格式
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.pojo.email)) {
        ElMessage.warning('邮箱格式不正确')
        return
      }

      // 验证用户名格式
      const usernameRegex = /^[a-zA-Z0-9]{4,20}$/
      if (!usernameRegex.test(this.pojo.username)) {
        ElMessage.warning('用户名必须是6-20位字母或数字')
        return
      }

      adminApi.saveOrUpdate(this.pojo.id, this.pojo).then(response => {
        ElMessage.success('保存成功')
        this.dialogVisible = false
        this.fetchData()

        // 如果是编辑当前登录用户的信息，同步更新导航栏
        this.updateCurrentUserInfo()
      }).catch(error => {
        // 错误已经在request.js中处理了，这里不需要重复显示
        console.error('保存失败:', error)
      })
    },

    // 删除
    handleDelete(id) {
      ElMessageBox.confirm('确定要删除这个管理员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adminApi.deleteById(id).then(response => {
          ElMessage.success('删除成功')
          this.fetchData()
        }).catch(error => {
          // 错误已经在request.js中处理了，这里不需要重复显示
          console.error('删除失败:', error)
        })
      }).catch(() => {
        ElMessage.info('已取消删除')
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedIds.length === 0) {
        ElMessage.warning('请选择要删除的管理员')
        return
      }

      ElMessageBox.confirm(`确定要删除选中的${this.selectedIds.length}个管理员吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adminApi.deleteBatch(this.selectedIds).then(response => {
          ElMessage.success('批量删除成功')
          this.selectedIds = []
          this.fetchData()
        }).catch(error => {
          // 错误已经在request.js中处理了，这里不需要重复显示
          console.error('批量删除失败:', error)
        })
      }).catch(() => {
        ElMessage.info('已取消删除')
      })
    },

    // 头像上传成功
    handleAvatarSuccess(response) {
      if (response.code === 0) {
        this.pojo.image = response.data
        ElMessage.success('头像上传成功')

        // 如果是编辑当前登录用户的头像，同步更新导航栏头像
        this.updateCurrentUserInfo()
      } else {
        ElMessage.error('头像上传失败：' + response.msg)
      }
    },

    // 头像上传前验证
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        ElMessage.error('头像图片只能是 JPG/PNG 格式!')
        return false
      }
      if (!isLt2M) {
        ElMessage.error('头像图片大小不能超过 2MB!')
        return false
      }
      return true
    },

    // 头像上传失败
    handleAvatarError(error, file, fileList) {
      console.error('头像上传失败:', error)
      console.log('当前token:', this.$store.getters.token)
      console.log('上传headers:', this.uploadHeaders)

      if (error.status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        this.$store.dispatch('user/resetToken')
        this.$router.push('/login')
      } else {
        ElMessage.error(`头像上传失败: ${error.message || '请重试'}`)
      }
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<style scoped>
.admin-container {
  padding: 24px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

/* 统计卡片行 */
.stats-row {
  margin-bottom: 24px;
}

.stats-card {
  position: relative;
  overflow: hidden;
}

.stats-content {
  position: relative;
  z-index: 2;
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

/* 表格样式 */
.table-card {
  margin-bottom: 24px;
}

.medical-table {
  :deep(.el-table) {
    th {
      background: #ffffff !important;
      background-image: none !important;
      background-color: #ffffff !important;
      color: #606266 !important;
      font-weight: 600;
      border-bottom: 1px solid #ebeef5 !important;
    }

    th.el-table__cell {
      background: #ffffff !important;
      background-image: none !important;
      background-color: #ffffff !important;
    }

    th .cell {
      color: #606266 !important;
      font-weight: 600;
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
  }
}

.avatar-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.user-avatar {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.name-cell {
  display: flex;
  align-items: center;
}

.user-name {
  font-weight: 500;
  color: #262626;
}

.user-info, .contact-info, .time-info, .role-info {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #595959;
}

.user-info i, .contact-info i, .time-info i, .role-info i {
  color: #1890ff;
  font-size: 14px;
}

.role-info {
  .role-names {
    color: #1890ff;
    font-weight: 500;
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
.pagination-card {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.medical-pagination {
  --el-pagination-bg-color: #ffffff;
  --el-pagination-text-color: #606266;
  --el-pagination-border-radius: 8px;
}

/* 对话框样式 */
.dialog-footer {
  text-align: right;
}

.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  background: #fafafa;
}

.avatar-uploader .avatar-uploader-icon:hover {
  border-color: #1890ff;
  color: #1890ff;
  background: #f0f8ff;
}

.avatar-uploader-el {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  background: #fafafa;
}

.avatar-uploader-el:hover {
  border-color: #1890ff;
  background: #f0f8ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-container {
    padding: 12px;
  }

  .stats-row .el-col {
    margin-bottom: 12px;
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
</style>
