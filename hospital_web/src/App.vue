<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
<style >
 .app-main{
    padding: 20px 40px;
  }
  .el-pagination{
    text-align: center;
    margin-top: 30px;
  }
  body .el-table th.gutter {
    display: table-cell !important;
  }
</style>