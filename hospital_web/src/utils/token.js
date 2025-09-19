/**
 * Token 工具类 - 简化版本
 * 前端只负责存储和发送token，校验由后端完成
 * @author Origami
 * @date 2025/9/10
 */

/**
 * 简单解析JWT token获取用户信息（仅用于前端显示）
 * 注意：这里只是为了前端显示用户信息，真正的验证在后端
 * @param {string} token
 * @returns {object} payload对象
 */
export function parseTokenForDisplay(token) {
  if (!token) return null

  try {
    const parts = token.split('.')
    if (parts.length !== 3) {
      return null
    }

    const payload = parts[1]
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )

    return JSON.parse(jsonPayload)
  } catch (error) {
    console.error('Token parsing failed:', error)
    return null
  }
}

/**
 * 获取token中的用户信息用于前端显示
 * @param {string} token
 * @returns {object} 用户信息
 */
export function getUserInfoFromToken(token) {
  const payload = parseTokenForDisplay(token)
  if (!payload) return null

  return {
    id: payload.id,
    username: payload.username,
    phone: payload.phone,
    image: payload.image
  }
}
