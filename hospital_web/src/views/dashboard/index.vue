<template>
  <div class="dashboard-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner fade-in-up">
      <div class="banner-content">
        <div class="welcome-text">
          <h1 class="welcome-title">
            <i class="el-icon-sunny"></i>
            欢迎回来，{{ name || '管理员' }}！
          </h1>
          <p class="welcome-subtitle">今天是 {{ currentDate }}，祝您工作愉快</p>
        </div>
        <div class="weather-info">
          <div class="weather-item">
            <i class="el-icon-sunny weather-icon"></i>
            <span>晴天 25°C</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview fade-in-up">
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stats-card primary">
            <div class="stats-content">
              <div class="stats-info">
                <h3 class="stats-number">{{ totalPatients }}</h3>
                <p class="stats-label">总患者数</p>
                <span class="stats-trend up">
                  <i class="el-icon-top"></i>
                  +12.5%
                </span>
              </div>
              <div class="stats-icon">
                <i class="el-icon-user"></i>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stats-card success">
            <div class="stats-content">
              <div class="stats-info">
                <h3 class="stats-number">{{ todayAppointments }}</h3>
                <p class="stats-label">今日预约</p>
                <span class="stats-trend up">
                  <i class="el-icon-top"></i>
                  +8.2%
                </span>
              </div>
              <div class="stats-icon">
                <i class="el-icon-date"></i>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stats-card warning">
            <div class="stats-content">
              <div class="stats-info">
                <h3 class="stats-number">{{ activeDoctors }}</h3>
                <p class="stats-label">在线医生</p>
                <span class="stats-trend down">
                  <i class="el-icon-bottom"></i>
                  -2.1%
                </span>
              </div>
              <div class="stats-icon">
                <i class="el-icon-user-solid"></i>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
          <div class="stats-card danger">
            <div class="stats-content">
              <div class="stats-info">
                <h3 class="stats-number">{{ revenue }}</h3>
                <p class="stats-label">今日收入</p>
                <span class="stats-trend up">
                  <i class="el-icon-top"></i>
                  +15.3%
                </span>
              </div>
              <div class="stats-icon">
                <i class="el-icon-money"></i>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions fade-in-up">
      <div class="medical-card">
        <div class="card-header">
          <h3 class="card-title quick-actions-title">
            <i class="el-icon-lightning"></i>
            快捷操作
          </h3>
        </div>
        <div class="actions-grid">
          <div class="action-item" @click="navigateTo('/doctor/admin')">
            <div class="action-icon primary">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="action-content">
              <h4>管理员管理</h4>
              <p>添加、编辑管理员信息</p>
            </div>
          </div>
          <div class="action-item" @click="navigateTo('/ks/department')">
            <div class="action-icon success">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="action-content">
              <h4>科室管理</h4>
              <p>管理医院科室信息</p>
            </div>
          </div>
          <div class="action-item" @click="navigateTo('/ks/consultingroom')">
            <div class="action-icon warning">
              <i class="el-icon-house"></i>
            </div>
            <div class="action-content">
              <h4>诊室管理</h4>
              <p>配置诊室和设备</p>
            </div>
          </div>
          <div class="action-item" @click="navigateTo('/banner/index')">
            <div class="action-icon info">
              <i class="el-icon-picture"></i>
            </div>
            <div class="action-content">
              <h4>轮播管理</h4>
              <p>管理首页轮播图</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
      // 统计数据
      totalPatients: 1248,
      todayAppointments: 86,
      activeDoctors: 24,
      revenue: '¥12,580',

      // 系统状态
      cpuUsage: 45,
      memoryUsage: 68,
      diskUsage: 32,

      // 最近活动
      recentActivities: [
        {
          id: 1,
          type: 'success',
          icon: 'el-icon-user',
          text: '新增管理员账号：张医生',
          time: '2分钟前'
        },
        {
          id: 2,
          type: 'primary',
          icon: 'el-icon-edit',
          text: '更新科室信息：心内科',
          time: '5分钟前'
        },
        {
          id: 3,
          type: 'warning',
          icon: 'el-icon-warning',
          text: '系统维护提醒',
          time: '10分钟前'
        },
        {
          id: 4,
          type: 'info',
          icon: 'el-icon-picture',
          text: '更新轮播图片',
          time: '15分钟前'
        }
      ]
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ]),
    currentDate() {
      const now = new Date()
      const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      }
      return now.toLocaleDateString('zh-CN', options)
    }
  },
  methods: {
    // 导航到指定页面
    navigateTo(path) {
      this.$router.push(path)
    },

    // 获取进度条颜色
    getProgressColor(percentage) {
      if (percentage < 50) return '#52c41a'
      if (percentage < 80) return '#faad14'
      return '#ff4d4f'
    }
  },

  mounted() {
    // 模拟实时数据更新
    setInterval(() => {
      this.cpuUsage = Math.floor(Math.random() * 100)
      this.memoryUsage = Math.floor(Math.random() * 100)
      this.diskUsage = Math.floor(Math.random() * 100)
    }, 5000)
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/variables.scss';

.dashboard-container {
  padding: 24px;
  background-color: $background;
  min-height: 100vh;
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 24px;
  color: white;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
    animation: float 6s ease-in-out infinite;
  }

  .banner-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    z-index: 2;
  }

  .welcome-title {
    font-size: 28px;
    font-weight: 700;
    margin: 0 0 8px 0;
    display: flex;
    align-items: center;

    i {
      margin-right: 12px;
      font-size: 32px;
      animation: pulse 2s infinite;
    }
  }

  .welcome-subtitle {
    font-size: 16px;
    opacity: 0.9;
    margin: 0;
  }

  .weather-info {
    display: flex;
    align-items: center;
  }

  .weather-item {
    display: flex;
    align-items: center;
    font-size: 16px;
    background: rgba(255, 255, 255, 0.2);
    padding: 12px 20px;
    border-radius: 25px;
    backdrop-filter: blur(10px);

    .weather-icon {
      margin-right: 8px;
      font-size: 20px;
    }
  }
}

/* 统计概览 */
.stats-overview {
  margin-bottom: 24px;
}

.stats-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  border-left: 4px solid;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  }

  &.primary {
    border-left-color: $primary;
    .stats-number, .stats-icon { color: $primary; }
  }

  &.success {
    border-left-color: $success;
    .stats-number, .stats-icon { color: $success; }
  }

  &.warning {
    border-left-color: $warning;
    .stats-number, .stats-icon { color: $warning; }
  }

  &.danger {
    border-left-color: $danger;
    .stats-number, .stats-icon { color: $danger; }
  }

  .stats-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .stats-number {
    font-size: 32px;
    font-weight: 700;
    margin: 0 0 8px 0;
    line-height: 1;
  }

  .stats-label {
    font-size: 14px;
    color: $text-secondary;
    margin: 0 0 8px 0;
  }

  .stats-trend {
    font-size: 12px;
    display: flex;
    align-items: center;
    font-weight: 500;

    &.up {
      color: $success;
    }

    &.down {
      color: $danger;
    }

    i {
      margin-right: 4px;
    }
  }

  .stats-icon {
    font-size: 48px;
    opacity: 0.3;
  }
}

/* 快捷操作 */
.quick-actions {
  margin-bottom: 24px;

  .quick-actions-title {
    margin-left: 12px;
  }

  .actions-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
    padding: 24px;
  }

  .action-item {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 12px;
    background: white;
    border: 2px solid transparent;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      border-color: $primary;
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(24, 144, 255, 0.15);
    }

    .action-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      font-size: 24px;
      color: white;

      &.primary { background: $primary; }
      &.success { background: $success; }
      &.warning { background: $warning; }
      &.info { background: $info; }
    }

    .action-content {
      h4 {
        margin: 0 0 4px 0;
        font-size: 16px;
        font-weight: 600;
        color: $text-primary;
      }

      p {
        margin: 0;
        font-size: 14px;
        color: $text-secondary;
      }
    }
  }
}

/* 底部区域 */
.bottom-section {
  margin-bottom: 24px;
}

/* 活动列表 */
.activity-list {
  padding: 24px;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }

  .activity-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    color: white;

    &.primary { background: $primary; }
    &.success { background: $success; }
    &.warning { background: $warning; }
    &.info { background: $info; }
  }

  .activity-content {
    flex: 1;
  }

  .activity-text {
    margin: 0 0 4px 0;
    font-size: 14px;
    color: $text-primary;
  }

  .activity-time {
    font-size: 12px;
    color: $text-secondary;
  }
}

/* 系统状态 */
.system-status {
  padding: 24px;
}

.status-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }

  .status-label {
    font-size: 14px;
    color: $text-primary;
    font-weight: 500;
    min-width: 100px;
  }

  .status-progress {
    flex: 1;
    margin-left: 20px;
  }

  .status-indicator {
    min-width: 80px;
    text-align: right;
  }
}

/* 动画效果 */
@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

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
  .dashboard-container {
    padding: 16px;
  }

  .welcome-banner {
    padding: 24px 20px;

    .banner-content {
      flex-direction: column;
      text-align: center;
    }

    .welcome-title {
      font-size: 24px;
      margin-bottom: 12px;
    }

    .weather-info {
      margin-top: 16px;
    }
  }

  .actions-grid {
    grid-template-columns: 1fr;
    padding: 16px;
  }

  .action-item {
    padding: 16px;
  }

  .stats-card {
    padding: 20px;
    margin-bottom: 16px;
  }
}
</style>
