/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.admin;

import mblog.data.Config;
import mblog.persist.service.ConfigService;
import mblog.persist.service.GroupService;
import mblog.persist.service.MenuService;
import mblog.persist.service.PostService;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 系统配置
 *
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/admin/config")
public class ConfigsController extends BaseController {
	@Autowired
	private EhCacheCacheManager ehcacheManager;
	@Autowired
	private ConfigService configService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private PostService postService;

	@RequestMapping("/")
	public String list(ModelMap model) {
		Collection<String> cacheNames = ehcacheManager.getCacheNames();

		model.put("cacheNames", cacheNames);
		model.put("configs", configService.findAll2Map());
		return "/admin/configs/main";
	}
	
	@RequestMapping("/update")
	public String update(HttpServletRequest request, ModelMap model) {
		Map<String, String[]> params = request.getParameterMap();

		List<Config> configs = new ArrayList<>();

		params.forEach((k, v) -> {
			Config conf = new Config();
			conf.setKey(k);
			conf.setValue(v[0]);

			configs.add(conf);
		});
		configService.update(configs);
		return "redirect:/admin/config/";
	}
	
	@RequestMapping("/flush_cache")
	public @ResponseBody Data flushCache() {
		ehcacheManager.getCacheManager().clearAll();
		return Data.success("操作成功", Data.NOOP);
	}

	@RequestMapping("/flush_conf")
	public @ResponseBody Data flushFiledia() {
		// 刷新系统变量
		List<Config> configs = configService.findAll();

		Map<String, String> configMap = new HashMap<>();
		configs.forEach(conf -> {
			servletContext.setAttribute(conf.getKey(), conf.getValue());
			configMap.put(conf.getKey(), conf.getValue());
		});

		appContext.setConfig(configMap);

		// 刷新文章Group
		servletContext.setAttribute("groups", groupService.findAll());

		// 刷新菜单
		servletContext.setAttribute("menus", menuService.findAll());
		return Data.success("操作成功", Data.NOOP);
	}

	@RequestMapping("/flush_indexs")
	public @ResponseBody Data flushIndexs() {
		postService.resetIndexs();
		return Data.success("操作成功", Data.NOOP);
	}
	
}
