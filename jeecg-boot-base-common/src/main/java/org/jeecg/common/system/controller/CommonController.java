package org.jeecg.common.system.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/common")
public class CommonController {

	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;

	/**
	 * 本地：local minio：minio 阿里：alioss
	 */
	@Value(value="${jeecg.uploadType}")
	private String uploadType;

	/**
	 * @Author 政辉
	 * @return
	 */
	@GetMapping("/403")
	public Result<?> noauth()  {
		return Result.error("没有权限，请联系管理员授权");
	}

	/**
	 * 文件上传统一方法
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/upload")
	public Result<?> upload(HttpServletRequest request, HttpServletResponse response) {
		Result<?> result = new Result<>();
		String savePath = "";
		Date d = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(d);
		String bizPath = date+ File.separator +request.getParameter("biz");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
		if(oConvertUtils.isEmpty(bizPath)){
			if(CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)){
				result.setMessage("使用阿里云文件上传时，必须添加目录！");
				result.setSuccess(false);
				return result;
			}else{
				bizPath = "";
			}
		}
		if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
			savePath = this.uploadLocal(file,bizPath);
		}else{
			savePath = sysBaseAPI.upload(file,bizPath,uploadType);
		}
		if("NotFormat".equals(savePath)){
			result.setMessage("图片格式不正确，只能上传png或者jpg格式的");
			result.setSuccess(false);
		}else if (oConvertUtils.isNotEmpty(savePath)){
			result.setMessage(savePath);
			result.setSuccess(true);
		}else {
			result.setMessage("上传失败！");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 本地文件上传
	 * @param mf 文件
	 * @param bizPath  自定义路径
	 * @return
	 */
	private String uploadLocal(MultipartFile mf,String bizPath){
		try {
			String ctxPath = uploadpath;
			String fileName = null;
			int lastIndexOf = mf.getOriginalFilename().lastIndexOf(".");
			String suffixs = mf.getOriginalFilename().substring(lastIndexOf);
			String path = ctxPath + File.separator + bizPath + File.separator;
			if (".apk".equals(suffixs)){
				path = ctxPath + File.separator +"temp"+File.separator+"apk"+File.separator;
			}
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();// 创建文件根目录
			}
			if(!suffixs.equals(".jpg")&&!suffixs.equals(".png")&&!suffixs.equals(".apk")){
				return "NotFormat";
			}
			String uuid = UUID.randomUUID().toString().replace("-","");
			uuid = uuid.substring(0,8);
			String orgName = mf.getOriginalFilename();// 获取文件名
			if (!".apk".equals(suffixs)){
			String suffix = orgName.substring(orgName.lastIndexOf("."));
				orgName = uuid + suffix;
				long timeMillis = System.currentTimeMillis();
				fileName = orgName.substring(0, orgName.lastIndexOf(".")) + String.valueOf(timeMillis).substring(6) + orgName.substring(orgName.indexOf("."));
			}else {
				fileName = orgName;
			}
			String savePath = file.getPath() + File.separator + fileName;
			File savefile = new File(savePath);
			FileCopyUtils.copy(mf.getBytes(), savefile);
			String dbpath = null;
			if (".apk".equals(suffixs)){
				dbpath = "apk"+File.separator+fileName;
			}else {
				if (oConvertUtils.isNotEmpty(bizPath)) {
					dbpath = bizPath + File.separator + fileName;
				} else {
					dbpath = fileName;
				}
			}
			if (dbpath.contains("\\")) {
				dbpath = dbpath.replace("\\", "/");
			}
			return dbpath;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}

//	@PostMapping(value = "/upload2")
//	public Result<?> upload2(HttpServletRequest request, HttpServletResponse response) {
//		Result<?> result = new Result<>();
//		try {
//			String ctxPath = uploadpath;
//			String fileName = null;
//			String bizPath = "files";
//			String tempBizPath = request.getParameter("biz");
//			if(oConvertUtils.isNotEmpty(tempBizPath)){
//				bizPath = tempBizPath;
//			}
//			String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
//			File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
//			if (!file.exists()) {
//				file.mkdirs();// 创建文件根目录
//			}
//			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//			MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
//			String orgName = mf.getOriginalFilename();// 获取文件名
//			fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
//			String savePath = file.getPath() + File.separator + fileName;
//			File savefile = new File(savePath);
//			FileCopyUtils.copy(mf.getBytes(), savefile);
//			String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
//			if (dbpath.contains("\\")) {
//				dbpath = dbpath.replace("\\", "/");
//			}
//			result.setMessage(dbpath);
//			result.setSuccess(true);
//		} catch (IOException e) {
//			result.setSuccess(false);
//			result.setMessage(e.getMessage());
//			log.error(e.getMessage(), e);
//		}
//		return result;
//	}

	/**
	 * 预览图片&下载文件
	 * 请求地址：http://localhost:8080/common/static/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
	 *
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/static/**")
	public void view(HttpServletRequest request, HttpServletResponse response) {
		// ISO-8859-1 ==> UTF-8 进行编码转换
		String imgPath = extractPathFromPattern(request);
		if (StringUtils.isEmpty(imgPath)){
			imgPath = "temp/apk/release-code2-v1.1.apk";
		}
		// 其余处理略
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			imgPath = imgPath.replace("..", "");
			if (imgPath.endsWith(",")) {
				imgPath = imgPath.substring(0, imgPath.length() - 1);
			}
			String filePath = uploadpath + File.separator + imgPath;
			String downloadFilePath = uploadpath + File.separator + filePath;
			File file = new File(downloadFilePath);
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
			inputStream = new BufferedInputStream(new FileInputStream(filePath));
			outputStream = response.getOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, len);
			}
			response.flushBuffer();
		} catch (IOException e) {
			log.error("预览文件失败" + e.getMessage());
			// e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

	}

//	/**
//	 * 下载文件
//	 * 请求地址：http://localhost:8080/common/download/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
//	 *
//	 * @param request
//	 * @param response
//	 * @throws Exception
//	 */
//	@GetMapping(value = "/download/**")
//	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		// ISO-8859-1 ==> UTF-8 进行编码转换
//		String filePath = extractPathFromPattern(request);
//		// 其余处理略
//		InputStream inputStream = null;
//		OutputStream outputStream = null;
//		try {
//			filePath = filePath.replace("..", "");
//			if (filePath.endsWith(",")) {
//				filePath = filePath.substring(0, filePath.length() - 1);
//			}
//			String localPath = uploadpath;
//			String downloadFilePath = localPath + File.separator + filePath;
//			File file = new File(downloadFilePath);
//	         if (file.exists()) {
//	         	response.setContentType("application/force-download");// 设置强制下载不打开            
//	 			response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
//	 			inputStream = new BufferedInputStream(new FileInputStream(file));
//	 			outputStream = response.getOutputStream();
//	 			byte[] buf = new byte[1024];
//	 			int len;
//	 			while ((len = inputStream.read(buf)) > 0) {
//	 				outputStream.write(buf, 0, len);
//	 			}
//	 			response.flushBuffer();
//	         }
//
//		} catch (Exception e) {
//			log.info("文件下载失败" + e.getMessage());
//			// e.printStackTrace();
//		} finally {
//			if (inputStream != null) {
//				try {
//					inputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (outputStream != null) {
//				try {
//					outputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}

	/**
	 * @功能：pdf预览Iframe
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/pdf/pdfPreviewIframe")
	public ModelAndView pdfPreviewIframe(ModelAndView modelAndView) {
		modelAndView.setViewName("pdfPreviewIframe");
		return modelAndView;
	}

	/**
	  *  把指定URL后的字符串全部截断当成参数
	  *  这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
	 * @param request
	 * @return
	 */
	private static String extractPathFromPattern(final HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}




	@GetMapping(value = "/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		try {

			String path = "temp/apk/honghong-release.apk";
			String uploadpaths = uploadpath.replace("\\","/");
			path = uploadpaths + "/" + path;
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			//String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"),"ISO-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			response.flushBuffer();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
