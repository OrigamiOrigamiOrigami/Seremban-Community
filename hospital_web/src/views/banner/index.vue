<template>

  <div>


  <el-form :inline="true">

          <el-form-item label="轮播图标题">
   <el-input v-model="searchMap.title" placeholder=""></el-input></el-form-item>

 <el-form-item label="活动区域">
    <el-select v-model="searchMap.position" placeholder="请选择广告位置">
      <el-option label="首页顶部" value="0"></el-option>
      <el-option label="首页中部" value="1"></el-option>
    </el-select>
  </el-form-item>

    <el-button type="primary" @click="fetchData()">查询</el-button>
    <el-button type="primary" @click="handleEdit('')">新增</el-button>
  </el-form>



  <!--表格组件-->
   <el-table
      :data="list"
      style="width: 100%">
    <!--prop 后端返回的json数据的字段名-->
    <el-table-column prop="id" label="id" ></el-table-column>
    <el-table-column prop="title" label="轮播图标题" ></el-table-column>
    <el-table-column prop="url" label="跳转连接" ></el-table-column>

    <el-table-column prop="image" label="缩略图" >
        <template scope="scope">
              <el-popover placement="right" title="" trigger="hover">
                <img :src="scope.row.image" style="height: 400px;width: 500px">
                <img slot="reference" :src="scope.row.image" :alt="scope.row.url" style="max-height: 50px;max-width: 130px">
              </el-popover>
       </template>
    </el-table-column>


    <el-table-column prop="position" label="广告位置">
              <template scope="scope">
                <span v-if="scope.row.position == 0">首页顶部</span>
                <span v-if="scope.row.position == 1">首页中部</span>
              </template>
    </el-table-column>

    <el-table-column prop="createTime" :formatter="dateFormat" label="创建时间"></el-table-column>

    <el-table-column
      fixed="right"
      label="操作"
    width="100">
    <template slot-scope="scope">
      <el-button @click="handleEdit(scope.row.id)" type="text" size="small">修改</el-button>
      <el-button @click="handleDelete(scope.row.id)" type="text" size="small">删除</el-button>
    </template>
    </el-table-column>
   </el-table>

    <!--分页-->
  <el-pagination
    @size-change="sizeData"
    @current-change="pageData"
    :page-sizes="[5,10,20,30,100]"
    :page-size="10"
    layout="total, sizes, prev, pager, next, jumper"
    :total="count">
  </el-pagination>


  <el-dialog title="编辑" :visible.sync="dialogFormVisible">
    <el-form label-width="80px">

     <el-form-item label="轮播图标题"><el-input v-model="pojo.title" /></el-form-item>
     <el-form-item label="跳转地址"><el-input v-model="pojo.url" /></el-form-item>
     <el-form-item label="广告位置">
       <el-select v-model="pojo.position" placeholder="请选择广告位置">
            <el-option v-for="item in positions" :key="item.id" :label="item.value" :value="item.id"></el-option>
        </el-select>
      </el-form-item>

       <el-form-item label="轮播图">
        <el-upload
        class="avatar-uploader"
        action="/api/admin/upload/image"
        :show-file-list="false"
        name="image"
        :headers="headers"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      </el-form-item>

      <el-button type="primary" @click="handleSave()">保存</el-button>
      <el-button @click="dialogFormVisible = false">关闭</el-button>
    </el-form>
  </el-dialog>





  </div>
</template>

<script>

   import bannerApi from "@/api/banner"

   //导入日期组件
   import dayjs from 'dayjs'

   import { getToken }  from '@/utils/auth'

    export default{
        data(){
            return{
                headers:{
                  token:getToken()
                },
                list:[],
                searchMap:{
                  page:1,
                  limit:10
                },
                count:0,
                pojo:{

                },
                dialogFormVisible:false,
                id:'',
                positions:[
                  {
                    'id':0,
                    'value':'首页顶部'
                  },
                  {
                    'id':1,
                    'value':'首页中部'
                  }
                ],
                imageUrl: ''
            }
        },
        created(){
           //在创建vue实例后触发初始化钩子函数
           this.fetchData();
        },
        methods:{
          fetchData(){
             //编写一个方法请求后端的接口，把返回的数据赋值list
            bannerApi.search(this.searchMap).then(response=>{
                   //把返回的结果赋值模型
                   this.list=response.data
                   this.count=response.count
            })
          },
          //当分页大小产生变化的时候调用该方法
          sizeData(size){
            this.searchMap.limit=size
            this.fetchData()
          },
          pageData(page){
            this.searchMap.page=page
            this.fetchData()
          },
          //新增和修改使用同一个函数打开弹窗，区别是新增没有id
          handleEdit(id){

              this.id=id

              //打开弹窗
              this.dialogFormVisible=true

              if(id !==''){

                 //根据id查询数据绑定到模型上面
                 bannerApi.findById(id).then(reponse =>{

                     this.pojo=reponse.data;
                     this.imageUrl=this.pojo.image
                 })

              }else{
                this.pojo={} //清空数据
                this.imageUrl='' //清空图片
              }



          },
          //新增和修改保存数据的方法
          handleSave(){


              //携带图片路径
              this.pojo.image=this.imageUrl

              bannerApi.update(this.id,this.pojo).then(reponse =>{

                   //新增或者修改成功
                   this.$message({
                    message:reponse.msg,
                    type:'success'
                   })

                   //重新加载列表数据
                   this.fetchData()

                   //关闭弹窗
                   this.dialogFormVisible=false

              })

          },
          handleDelete(id){

            this.$confirm('您确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {

                bannerApi.deleteById(id).then(response=>{
                    this.$message({
                     type: 'success',
                     message: '删除成功!'
                   });
                   this.fetchData()
                })

              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消删除'
                });
              });

          },
          handleAvatarSuccess(res, file) {
                //把上传成功的图片绑定到模型上
                this.imageUrl = res.data;
              },
              beforeAvatarUpload(file) {

                const isLt2M = file.size / 1024 / 1024 < 5;


                if (!isLt2M) {
                  this.$message.error('轮播图大小不能超过 5MB!');
                }
                return  isLt2M;
              },
          //增加格式化日期的方法
          dateFormat(row,column){

            let data=row[column.property]
            if(data == undefined){
              return ''
            }
            return dayjs(data).format('YYYY-MM-DD HH:mm:ss')
          }

        }
    }


</script>
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
