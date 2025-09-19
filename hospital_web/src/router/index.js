import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [{
      path: 'index',
      name: 'Profile',
      component: () => import('@/views/profile/index'),
      meta: { title: '个人中心', icon: 'user' }
    }]
  },
  {
    path: '/doctor',
    component: Layout,
    redirect: '/doctor/admin',
    name: 'doctor',
    meta: { title: '系统管理', icon: 'el-icon-tableware' },
    alwaysShow: true,
    children: [
      {
        path: 'admin',
        name: 'admin',
        component: () => import('@/views/admin/index'),
        meta: { title: '管理员管理', icon: 'el-icon-picture' }
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/views/role/index'),
        meta: { title: '角色管理', icon: 'el-icon-picture' }
      },
      {
        path: 'permission',
        name: 'permission',
        component: () => import('@/views/permission/index'),
        meta: { title: '权限管理', icon: 'el-icon-picture' }
      }
    ]
  },
  {
    path: '/ks',
    component: Layout,
    redirect: '/ks/department',
    name: 'mediacal',
    meta: { title: '科室管理', icon: 'el-icon-coffee-cup' },
    alwaysShow: true,
    children: [
      {
        path: 'department',
        name: 'department',
        component: () => import('@/views/department/index'),
        meta: { title: '科室管理', icon: 'el-icon-grape' }
      },
      {
        path: 'consultingroom',
        name: 'consultingroom',
        component: () => import('@/views/consultingroom/index'),
        meta: { title: '诊室管理', icon: 'el-icon-ice-cream-round' }
      }
    ]
  },
  {
    path: '/banner',
    component: Layout,
    redirect: '/banner/index',
    name: 'banner',
    meta: { title: '轮播管理', icon: 'el-icon-picture' },
    alwaysShow: true,
    children: [
      {
        path: 'index',
        name: 'bannerIndex',
        component: () => import('@/views/banner/index'),
        meta: { title: '轮播管理', icon: 'el-icon-picture' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '/:pathMatch(.*)*', redirect: '/404', hidden: true }
]

const router = createRouter({
  history: createWebHashHistory(),
  scrollBehavior: () => ({ top: 0 }),
  routes: constantRoutes
})

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  // Vue Router 4.x 的重置方法
  router.getRoutes().forEach(route => {
    if (route.name) {
      router.removeRoute(route.name)
    }
  })
  constantRoutes.forEach(route => {
    router.addRoute(route)
  })
}

export default router
