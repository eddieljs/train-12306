const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    devServer: {
      // 报错了不会全屏显示
      client: {overlay: false}
    }
  }

})


// module.exports = {
//   devServer: {
//     port: 10001,
//     proxy: {
//       '/api': {
//         target: 'http://localhost:8000', // 后端服务地址
//         changeOrigin: true, // 是否改变源
//         pathRewrite: {
//           '/api': '' // 重写路径: 去掉路径中的/api
//         }
//       }
//     }
//   }
// }
