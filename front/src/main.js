import Vue from 'vue'
import App from './App.vue'
import NProgress from 'vue-nprogress'
import Vuex from 'vuex'
import router from './router'
import resource from 'vue-resource';

// import './assets/customized.sass';

Vue.use(NProgress)
Vue.use(Vuex)
Vue.use(resource)

// const nprogress = new NProgress({parent: '.nprogress-container'})


const app = new Vue({
	// nprogress,
	router,
	...App
});
export {app}