import adminApi from '@/api/admin'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      adminApi.login({ username: username.trim(), password: password }).then(response => {
        //获取token
        const { data: token } = response

        //存储token
        commit('SET_TOKEN', token)
        setToken(token)
        resolve()

      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      // 从token中解析用户信息用于显示
      try {
        const { getUserInfoFromToken } = require('@/utils/token')
        const userInfo = getUserInfoFromToken(state.token)

        if (userInfo) {
          commit('SET_NAME', userInfo.username)
          commit('SET_AVATAR', userInfo.image || '')
          resolve(userInfo)
        } else {
          reject('Token解析失败')
        }
      } catch (error) {
        reject(error)
      }
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()

    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

