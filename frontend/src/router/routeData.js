
let routeData = null

export const setRouteData = (data) => {
  routeData = data
}

export const getRouteData = () => {
  const data = routeData
  routeData = null  // 사용 후 데이터 삭제
  return data
}
