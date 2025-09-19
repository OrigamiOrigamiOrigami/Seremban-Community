/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  // 验证用户名格式：4-20位字母数字组合，与后端验证规则保持一致
  const usernameRegex = /^[a-zA-Z0-9]{4,20}$/
  return str && str.trim() && usernameRegex.test(str.trim())
}
