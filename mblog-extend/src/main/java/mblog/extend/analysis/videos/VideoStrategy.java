/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.extend.analysis.videos;

import mblog.extend.data.Video;

/**
 * @author langhsu
 *
 */
public interface VideoStrategy {
	
	/**
	 * 获取视频信息
	 * 
	 * @param url 视频地址
	 * @return
	 */
	Video take(String url);
}
