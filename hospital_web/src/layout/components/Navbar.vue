<template>
  <div class="navbar">
    <div class="navbar-left">
      <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
      <breadcrumb class="breadcrumb-container" />
    </div>

    <div class="navbar-center">
      <div class="system-title">
        <i class="el-icon-s-help system-icon"></i>
        <span>芙蓉医疗互联网+平台</span>
      </div>
    </div>

    <div class="right-menu">
      <!-- 通知铃铛 -->
      <div class="notification-container">
        <el-button type="text" class="notification-btn">
          <i class="el-icon-bell"></i>
        </el-button>
      </div>

      <!-- 全屏按钮 -->
      <div class="fullscreen-container">
        <el-button type="text" class="fullscreen-btn" @click="toggleFullscreen">
          <i :class="isFullscreen ? 'el-icon-aim' : 'el-icon-full-screen'"></i>
        </el-button>
      </div>

      <!-- 用户头像下拉菜单 -->
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <el-avatar
            v-if="avatar"
            :src="avatar"
            :size="36"
            class="user-avatar">
          </el-avatar>
          <el-avatar v-else :size="36" class="user-avatar">
            <i class="el-icon-user-solid"></i>
          </el-avatar>
          <div class="user-info">
            <span class="user-name">{{ name || '管理员' }}</span>
            <span class="user-role">系统管理员</span>
          </div>
          <i class="el-icon-caret-bottom dropdown-icon" />
        </div>
        <template #dropdown>
          <el-dropdown-menu class="user-dropdown">
            <el-dropdown-item @click="goToProfile">
              <i class="el-icon-user"></i>
              <span>个人中心</span>
            </el-dropdown-item>
            <el-dropdown-item>
              <i class="el-icon-setting"></i>
              <span>系统设置</span>
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <i class="el-icon-switch-button"></i>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
      isFullscreen: false
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'name'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },

    // 切换全屏
    toggleFullscreen() {
      if (!document.fullscreenElement) {
        document.documentElement.requestFullscreen()
        this.isFullscreen = true
      } else {
        if (document.exitFullscreen) {
          document.exitFullscreen()
          this.isFullscreen = false
        }
      }
    },

    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },

    // 跳转到个人中心
    goToProfile() {
      this.$router.push('/profile')
    }
  },

  mounted() {
    // 监听全屏状态变化
    document.addEventListener('fullscreenchange', () => {
      this.isFullscreen = !!document.fullscreenElement
    })
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.navbar {
  height: 60px;
  overflow: hidden;
  position: relative;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #e8e8e8;

  .navbar-left {
    display: flex;
    align-items: center;
    flex: 1;
  }

  .navbar-center {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 1;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    pointer-events: none;
  }

  .system-title {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
    color: $primary;

    .system-icon {
      margin-right: 8px;
      font-size: 24px;
    }
  }

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    cursor: pointer;
    transition: all 0.3s;
    padding: 8px;
    border-radius: 8px;
    margin-right: 16px;

    &:hover {
      background: rgba(24, 144, 255, 0.1);
      color: $primary;
    }
  }

  .breadcrumb-container {
    flex: 1;
  }

  .right-menu {
    display: flex;
    align-items: center;
    height: 100%;
    gap: 16px;

    .notification-container,
    .fullscreen-container {
      position: relative;
    }

    .notification-btn,
    .fullscreen-btn {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      color: #606266;
      transition: all 0.3s ease;
      border: none;
      background: transparent;

      &:hover {
        background: rgba(24, 144, 255, 0.1);
        color: $primary;
        transform: scale(1.1);
      }
    }

    .avatar-container {
      .avatar-wrapper {
        display: flex;
        align-items: center;
        cursor: pointer;
        padding: 8px 12px;
        border-radius: 12px;
        transition: all 0.3s ease;
        background: rgba(24, 144, 255, 0.05);
        border: 1px solid rgba(24, 144, 255, 0.1);

        &:hover {
          background: rgba(24, 144, 255, 0.1);
          border-color: rgba(24, 144, 255, 0.2);
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(24, 144, 255, 0.15);
        }

        .user-avatar {
          margin-right: 12px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          transition: all 0.3s ease;

          &:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          }
        }

        .user-info {
          display: flex;
          flex-direction: column;
          margin-right: 8px;
        }

        .user-name {
          font-size: 14px;
          font-weight: 500;
          color: $text-primary;
          line-height: 1.2;
        }

        .user-role {
          font-size: 12px;
          color: $text-secondary;
          line-height: 1.2;
        }

        .dropdown-icon {
          font-size: 12px;
          color: $text-secondary;
          transition: all 0.3s ease;
        }

        &:hover .dropdown-icon {
          color: $primary;
        }
      }
    }
  }
}
</style>
