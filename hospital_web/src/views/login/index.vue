<template>
  <div class="login-container">
    <!-- 动态背景 -->
    <div class="bg-animation">
      <div class="bg-particle" v-for="i in 50" :key="i" :style="getParticleStyle(i)"></div>
    </div>

    <!-- 左侧装饰区域 -->
    <div class="left-section">
      <div class="brand-info">
        <div class="brand-logo">
          <div class="logo-circle">
            <i class="el-icon-s-help"></i>
          </div>
        </div>
        <h1 class="brand-title">芙蓉医疗互联网+平台</h1>
        <p class="brand-subtitle">智慧医疗 · 数字化管理 · 高效服务</p>
        <div class="feature-list">
          <div class="feature-item">
            <i class="el-icon-check"></i>
            <span>安全可靠的数据管理</span>
          </div>
          <div class="feature-item">
            <i class="el-icon-check"></i>
            <span>智能化医疗服务</span>
          </div>
          <div class="feature-item">
            <i class="el-icon-check"></i>
            <span>高效的工作流程</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧登录区域 -->
    <div class="right-section">
      <div class="login-wrapper">
        <div class="login-header">
          <h2>欢迎登录</h2>
          <p>请输入您的账号信息</p>
        </div>

        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on">

          <el-form-item prop="username" class="form-item">
            <div class="input-group">
              <el-input
                ref="username"
                v-model="loginForm.username"
                placeholder="用户名"
                name="username"
                type="text"
                tabindex="1"
                auto-complete="on"
                class="custom-input"
              >
                <template #prefix>
                  <i class="el-icon-user input-icon"></i>
                </template>
              </el-input>
            </div>
          </el-form-item>

          <el-form-item prop="password" class="form-item">
            <div class="input-group">
              <el-input
                :key="passwordType"
                ref="password"
                v-model="loginForm.password"
                :type="passwordType"
                placeholder="密码"
                name="password"
                tabindex="2"
                auto-complete="on"
                class="custom-input"
                @keyup.enter.native="handleLogin"
              >
                <template #prefix>
                  <i class="el-icon-lock input-icon"></i>
                </template>
                <template #suffix>
                  <div class="password-toggle" @click="showPwd">
                    <i :class="passwordType === 'password' ? 'el-icon-view' : 'el-icon-hide'"></i>
                  </div>
                </template>
              </el-input>
            </div>
          </el-form-item>

          <el-button
            :loading="loading"
            type="primary"
            class="login-button"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">立即登录</span>
            <span v-else>登录中...</span>
          </el-button>

          <div class="login-info">
            <div class="demo-account">
              <i class="el-icon-info"></i>
              <span>测试账号：admin / 123456</span>
            </div>
          </div>

        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value || !value.trim()) {
        callback(new Error('用户名不能为空'))
      } else if (!validUsername(value)) {
        callback(new Error('用户名格式不正确，请输入4-20位字母数字组合'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('密码至少需要5位长度'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          //向后端发起登录请求  1.请求路径   2.提交参数用户名和密码
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            // 登录成功后获取用户信息
            return this.$store.dispatch('user/getInfo')
          }).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getParticleStyle(index) {
      const size = Math.random() * 4 + 2
      const duration = Math.random() * 20 + 10
      const delay = Math.random() * 20
      return {
        width: size + 'px',
        height: size + 'px',
        left: Math.random() * 100 + '%',
        top: Math.random() * 100 + '%',
        animationDuration: duration + 's',
        animationDelay: delay + 's'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  position: relative;
  overflow: hidden;

  // 动态背景粒子
  .bg-animation {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;

    .bg-particle {
      position: absolute;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 50%;
      animation: particleFloat 15s infinite linear;
    }
  }

  // 左侧品牌区域
  .left-section {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 60px;
    z-index: 2;

    .brand-info {
      max-width: 500px;
      color: white;
      text-align: center;

      .brand-logo {
        margin-bottom: 40px;

        .logo-circle {
          width: 100px;
          height: 100px;
          background: rgba(255, 255, 255, 0.15);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin: 0 auto;
          backdrop-filter: blur(10px);
          border: 2px solid rgba(255, 255, 255, 0.2);

          i {
            font-size: 40px;
            color: white;
          }
        }
      }

      .brand-title {
        font-size: 36px;
        font-weight: 700;
        margin: 0 0 16px 0;
        text-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        letter-spacing: 1px;
      }

      .brand-subtitle {
        font-size: 18px;
        opacity: 0.9;
        margin: 0 0 50px 0;
        font-weight: 300;
      }

      .feature-list {
        text-align: left;
        max-width: 300px;
        margin: 0 auto;

        .feature-item {
          display: flex;
          align-items: center;
          margin-bottom: 20px;
          font-size: 16px;

          i {
            width: 24px;
            height: 24px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            font-size: 12px;
          }

          span {
            opacity: 0.95;
          }
        }
      }
    }
  }

  // 右侧登录区域
  .right-section {
    width: 480px;
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 2;
    box-shadow: -10px 0 30px rgba(0, 0, 0, 0.1);

    .login-wrapper {
      width: 100%;
      max-width: 360px;
      padding: 40px;

      .login-header {
        text-align: center;
        margin-bottom: 40px;

        h2 {
          font-size: 28px;
          font-weight: 600;
          color: #2c3e50;
          margin: 0 0 8px 0;
        }

        p {
          color: #7f8c8d;
          font-size: 15px;
          margin: 0;
        }
      }

      .login-form {
        .form-item {
          margin-bottom: 24px;

          .input-group {
            position: relative;
            background: white;
            border: 2px solid #e9ecef;
            border-radius: 12px;
            width: 100%;
            transition: all 0.3s ease;

            &:hover {
              border-color: #667eea;
              box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.05);
            }

            &:focus-within {
              border-color: #667eea;
              background: white;
              box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
            }

            .custom-input {
              width: 100%;

              :deep(.el-input__wrapper) {
                border: none;
                background: transparent;
                box-shadow: none;
                padding: 0;
                width: 100%;
              }

              :deep(.el-input__inner) {
                border: none;
                background: transparent;
                padding: 15px;
                font-size: 16px;
                color: #2c3e50;
                height: 50px;
                width: 100%;

                &::placeholder {
                  color: #adb5bd;
                }

                &:focus {
                  box-shadow: none;
                }
              }

              :deep(.el-input__prefix) {
                padding-left: 15px;
                display: flex;
                align-items: center;

                .input-icon {
                  color: #6c757d;
                  font-size: 18px;
                }
              }

              :deep(.el-input__suffix) {
                padding-right: 15px;
                display: flex;
                align-items: center;

                .password-toggle {
                  cursor: pointer;
                  color: #6c757d;
                  font-size: 18px;

                  &:hover {
                    color: #667eea;
                  }
                }
              }
            }
          }
        }

        .login-button {
          width: 100%;
          height: 52px;
          font-size: 16px;
          font-weight: 600;
          border-radius: 12px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border: none;
          margin-top: 8px;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
          }

          &:active {
            transform: translateY(0);
          }
        }

        .login-info {
          margin-top: 30px;

          .demo-account {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 12px 16px;
            background: #e8f4fd;
            border-radius: 8px;
            color: #1890ff;
            font-size: 14px;

            i {
              margin-right: 8px;
              font-size: 16px;
            }
          }
        }
      }
    }
  }
}

// 粒子动画
@keyframes particleFloat {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) rotate(360deg);
    opacity: 0;
  }
}

// 响应式设计
@media (max-width: 1024px) {
  .login-container {
    .left-section {
      display: none;
    }

    .right-section {
      width: 100%;
    }
  }
}

@media (max-width: 768px) {
  .login-container {
    .right-section {
      .login-wrapper {
        padding: 30px 20px;

        .login-header {
          margin-bottom: 30px;

          h2 {
            font-size: 24px;
          }
        }
      }
    }
  }
}

// Element UI 样式重置
:deep(.el-form-item__error) {
  color: #e74c3c;
  font-size: 12px;
  margin-top: 5px;
}

:deep(.el-form-item.is-error .input-group) {
  border-color: #e74c3c;
  background: white;
}

:deep(.el-button.is-loading) {
  pointer-events: none;
}
</style>
