<template>
  <div class="profile-container">
    <!-- 页面头部 -->
    <div class="medical-card page-header fade-in-up">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user"></i>
            个人中心
          </h2>
          <p class="page-description">管理您的个人信息和账户设置</p>
        </div>
        <div class="header-right">
          <div class="user-avatar-large">
            <el-avatar
              v-if="userInfo.image"
              :src="userInfo.image"
              :size="80"
              class="avatar-large">
            </el-avatar>
            <el-avatar v-else :size="80" class="avatar-large">
              <i class="el-icon-user-solid"></i>
            </el-avatar>
          </div>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <!-- 个人信息卡片 -->
      <div class="medical-card info-card fade-in-up">
        <div class="card-header">
          <h3 class="card-title">
            <i class="el-icon-edit"></i>
            个人信息
          </h3>
          <el-button type="primary" @click="editInfo = true" class="edit-btn">
            <i class="el-icon-edit"></i>
            编辑信息
          </el-button>
        </div>

        <div class="info-content">
          <el-form :model="userInfo" label-width="100px" class="info-form">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="头像">
                  <div class="avatar-section">
                    <el-avatar
                      v-if="userInfo.image"
                      :src="userInfo.image"
                      :size="60"
                      class="info-avatar">
                    </el-avatar>
                    <el-avatar v-else :size="60" class="info-avatar">
                      <i class="el-icon-user-solid"></i>
                    </el-avatar>
                    <el-button
                      v-if="editInfo"
                      type="text"
                      @click="showAvatarUpload = true"
                      class="change-avatar-btn">
                      更换头像
                    </el-button>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="姓名">
                  <el-input
                    v-if="editInfo"
                    v-model="userInfo.name"
                    placeholder="请输入姓名">
                  </el-input>
                  <span v-else class="info-text">{{ userInfo.name || '未设置' }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="用户名">
                  <el-input
                    v-if="editInfo"
                    v-model="userInfo.username"
                    placeholder="请输入用户名">
                  </el-input>
                  <span v-else class="info-text">{{ userInfo.username || '未设置' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号码">
                  <el-input
                    v-if="editInfo"
                    v-model="userInfo.phone"
                    placeholder="请输入手机号码">
                  </el-input>
                  <span v-else class="info-text">{{ userInfo.phone || '未设置' }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input
                    v-if="editInfo"
                    v-model="userInfo.email"
                    placeholder="请输入邮箱">
                  </el-input>
                  <span v-else class="info-text">{{ userInfo.email || '未设置' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="角色">
                  <div class="role-info">
                    <span v-if="userInfo.roleNames" class="info-text role-names">{{ userInfo.roleNames }}</span>
                    <el-tag v-else type="info" size="small">未分配角色</el-tag>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="创建时间">
                  <span class="info-text">{{ formatDate(userInfo.created) }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item v-if="editInfo" class="form-actions">
              <el-button type="primary" @click="saveUserInfo" :loading="saving">
                <i class="el-icon-check"></i>
                保存修改
              </el-button>
              <el-button @click="cancelEdit">
                <i class="el-icon-close"></i>
                取消
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 密码修改卡片 -->
      <div class="medical-card password-card fade-in-up">
        <div class="card-header">
          <h3 class="card-title">
            <i class="el-icon-lock"></i>
            修改密码
          </h3>
        </div>

        <div class="password-content">
          <el-form
            :model="passwordForm"
            :rules="passwordRules"
            ref="passwordForm"
            label-width="120px"
            class="password-form">
            <el-form-item label="当前密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入当前密码"
                show-password>
              </el-input>
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password>
              </el-input>
            </el-form-item>

            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password>
              </el-input>
            </el-form-item>

            <el-form-item class="form-actions">
              <el-button type="primary" @click="changePassword" :loading="changingPassword">
                <i class="el-icon-key"></i>
                修改密码
              </el-button>
              <el-button @click="resetPasswordForm">
                <i class="el-icon-refresh"></i>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 头像上传对话框 -->
    <el-dialog title="更换头像" v-model="showAvatarUpload" width="400px">
      <div class="avatar-upload-dialog">
        <el-upload
          class="avatar-uploader"
          :action="uploadAction"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :on-error="handleAvatarError"
          :before-upload="beforeAvatarUpload">
          <img v-if="userInfo.image" :src="userInfo.image" class="avatar-preview">
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <div class="upload-tips">
          <p>支持 JPG、PNG 格式</p>
          <p>文件大小不超过 2MB</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="showAvatarUpload = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import adminApi from '@/api/admin'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

export default {
  name: 'Profile',
  components: {
    Plus
  },
  data() {
    // 确认密码验证
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    return {
      userInfo: {
        id: null,
        name: '',
        username: '',
        phone: '',
        email: '',
        image: '',
        created: null,
        roles: [],
        roleNames: ''
      },
      originalUserInfo: {},
      editInfo: false,
      saving: false,
      showAvatarUpload: false,

      // 密码修改表单
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入当前密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度至少6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      changingPassword: false,

      // 上传配置
      uploadAction: process.env.VUE_APP_BASE_API + '/upload/avatar'
    }
  },
  computed: {
    uploadHeaders() {
      const token = this.$store.getters.token
      if (token) {
        return {
          'Authorization': 'Bearer ' + token,
          'token': token
        }
      }
      return {}
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    // 加载用户信息
    loadUserInfo() {
      // 直接调用后端的当前用户信息接口
      adminApi.getCurrentUser().then(response => {
        if (response.code === 0) {
          this.userInfo = { ...response.data }
          this.originalUserInfo = { ...response.data }
        } else {
          ElMessage.error('获取用户信息失败：' + response.msg)
        }
      }).catch(error => {
        ElMessage.error('获取用户信息失败：' + error.message)
      })
    },

    // 保存用户信息
    saveUserInfo() {
      this.saving = true
      adminApi.update(this.userInfo).then(response => {
        if (response.code === 0) {
          ElMessage.success('保存成功')
          this.editInfo = false
          this.originalUserInfo = { ...this.userInfo }

          // 更新导航栏信息
          this.$store.commit('user/SET_NAME', this.userInfo.name)
          this.$store.commit('user/SET_AVATAR', this.userInfo.image)
        } else {
          ElMessage.error('保存失败：' + response.msg)
        }
      }).catch(error => {
        ElMessage.error('保存失败：' + error.message)
      }).finally(() => {
        this.saving = false
      })
    },

    // 取消编辑
    cancelEdit() {
      this.userInfo = { ...this.originalUserInfo }
      this.editInfo = false
    },

    // 修改密码
    changePassword() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.changingPassword = true

          // 调用修改密码API
          adminApi.changePassword({
            oldPassword: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword,
            confirmPassword: this.passwordForm.confirmPassword
          }).then(response => {
            if (response.code === 0) {
              ElMessage.success('密码修改成功，请重新登录')
              this.resetPasswordForm()

              // 密码修改成功后，提示用户重新登录
              this.$confirm('密码已修改成功，需要重新登录', '提示', {
                confirmButtonText: '重新登录',
                cancelButtonText: '稍后再说',
                type: 'success'
              }).then(() => {
                this.$store.dispatch('user/logout').then(() => {
                  this.$router.push('/login')
                })
              })
            } else {
              ElMessage.error('密码修改失败：' + response.msg)
            }
          }).catch(error => {
            ElMessage.error('密码修改失败：' + error.message)
          }).finally(() => {
            this.changingPassword = false
          })
        }
      })
    },

    // 重置密码表单
    resetPasswordForm() {
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.$refs.passwordForm && this.$refs.passwordForm.clearValidate()
    },

    // 头像上传成功
    handleAvatarSuccess(response) {
      if (response.code === 0) {
        this.userInfo.image = response.data
        this.$store.commit('user/SET_AVATAR', response.data)
        ElMessage.success('头像上传成功')
        this.showAvatarUpload = false
      } else {
        ElMessage.error('头像上传失败：' + response.msg)
      }
    },

    // 头像上传失败
    handleAvatarError(error) {
      console.error('头像上传失败:', error)
      if (error.status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        this.$store.dispatch('user/resetToken')
        this.$router.push('/login')
      } else {
        ElMessage.error('头像上传失败，请重试')
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

    // 格式化日期
    formatDate(date) {
      if (!date) return '未知'
      return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;
    padding: 30px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          font-size: 28px;
          font-weight: 600;
          color: #1890ff;
          margin: 0 0 8px 0;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 32px;
          }
        }

        .page-description {
          font-size: 16px;
          color: #666;
          margin: 0;
        }
      }

      .header-right {
        .user-avatar-large {
          .avatar-large {
            border: 4px solid #fff;
            box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
          }
        }
      }
    }
  }

  .profile-content {
    display: flex;
    flex-direction: column;
    gap: 20px;

    .medical-card {
      background: #fff;
      border-radius: 12px;
      padding: 0;
      overflow: hidden;

      .card-header {
        padding: 24px 30px;
        border-bottom: 1px solid #f0f0f0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: linear-gradient(135deg, #f8f9ff 0%, #ffffff 100%);

        .card-title {
          font-size: 20px;
          font-weight: 600;
          color: #1890ff;
          margin: 0;
          display: flex;
          align-items: center;

          i {
            margin-right: 8px;
            font-size: 22px;
          }
        }

        .edit-btn {
          border-radius: 8px;
          padding: 8px 16px;
        }
      }

      .info-content,
      .password-content {
        padding: 30px;
      }

      .info-form,
      .password-form {
        .el-form-item {
          margin-bottom: 24px;

          .el-form-item__label {
            font-weight: 500;
            color: #333;
          }

          .info-text {
            font-size: 14px;
            color: #666;
            line-height: 32px;
          }

          .avatar-section {
            display: flex;
            align-items: center;
            gap: 16px;

            .info-avatar {
              border: 2px solid #f0f0f0;
            }

            .change-avatar-btn {
              color: #1890ff;
              font-size: 14px;
            }
          }

          .role-info {
            .role-names {
              color: #1890ff;
              font-weight: 500;
            }
          }
        }

        .form-actions {
          margin-top: 32px;
          margin-bottom: 0;

          .el-button {
            border-radius: 8px;
            padding: 10px 20px;
            margin-right: 12px;
          }
        }
      }
    }
  }
}

// 头像上传对话框样式
.avatar-upload-dialog {
  text-align: center;

  .avatar-uploader {
    .el-upload {
      border: 2px dashed #d9d9d9;
      border-radius: 12px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      width: 200px;
      height: 200px;
      margin: 0 auto;
      transition: all 0.3s ease;

      &:hover {
        border-color: #1890ff;
        transform: scale(1.02);
      }

      .avatar-preview {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .avatar-uploader-icon {
        font-size: 48px;
        color: #8c939d;
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }

  .upload-tips {
    margin-top: 16px;

    p {
      margin: 4px 0;
      font-size: 14px;
      color: #999;
    }
  }
}

// 动画效果
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

// 响应式设计
@media (max-width: 768px) {
  .profile-container {
    padding: 16px;

    .page-header {
      padding: 20px;

      .header-content {
        flex-direction: column;
        text-align: center;
        gap: 20px;

        .header-left {
          .page-title {
            font-size: 24px;
          }
        }
      }
    }

    .medical-card {
      .card-header {
        padding: 20px;
        flex-direction: column;
        gap: 16px;
        text-align: center;
      }

      .info-content,
      .password-content {
        padding: 20px;
      }

      .info-form,
      .password-form {
        .el-row {
          .el-col {
            margin-bottom: 16px;
          }
        }
      }
    }
  }
}
</style>
