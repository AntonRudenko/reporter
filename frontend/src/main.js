import Vue from 'vue'
import App from './App.vue'
import Report from './components/Report'
import ReportList from './components/ReportList'
import About from './components/About'
import Test from './components/Test'

import '@/assets/css/tailwind.css'
import VueRouter from 'vue-router'

import axios from "axios";

Vue.use(VueRouter)
Vue.config.productionTip = false


const router = new VueRouter({
  routes: [
    { path: '/report/:name', component: Report },
    { path: '/report', component: ReportList },
    { path: '/test', component: Test },
    { path: '/about', component: About}
  ]
})

axios.defaults.baseURL = 'http://127.0.0.1:18080/report/'

new Vue({
  render: h => h(App),
  router
}).$mount('#app')
