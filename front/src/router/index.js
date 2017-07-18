import Vue from 'vue'
import Router from 'vue-router'
// import menuModule from 'vuex-store/modules/menu'
Vue.use(Router)

export default new Router({
	mode: 'hash', // Demo is living in GitHub.io, so required!
	linkActiveClass: 'is-active',
	scrollBehavior: () => ({ y: 0 }),
	routes: [
		{
			name: 'Home',
			path: '/',
			component: require('../views/Home.vue')
		},
		// ...generateRoutesFromMenu(menuModule.state.items),
		// {
		// 	path: '*',
		// 	redirect: '/'
		// },
		{
			name: 'Login',
			path: '/login',
			component: require('../views/Login.vue')
		},
		{
			name: 'Admin',
			path: '/admin',
			component: require('../views/Admin.vue'),
			children: [
				{
                    path:'goods',
                    component: require('../component/Goods/Goods.vue')
                },
                {
                    path:'log/:id',
                    component: require('../component/Log.vue')
                },
				{
					path:'equipment',
					component: require('../component/Equipment/Equipment.vue')
				},
				{
					path:'statistics',
					component: require('../component/Statistics/Statistics.vue')
				}
			]
		}
	]
})

// Menu should have 2 levels.
function generateRoutesFromMenu (menu = [], routes = []) {
	for (let i = 0, l = menu.length; i < l; i++) {
		let item = menu[i]
		if (item.path) {
			routes.push(item)
		}
		if (!item.component) {
			generateRoutesFromMenu(item.children, routes)
		}
	}
	return routes
}
