package com.ssafy.group5.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssafy.group5.dto.Attraction;
import com.ssafy.group5.dto.Myplace;
import com.ssafy.group5.model.service.AttractionService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/trip")
@Slf4j
public class AttractionController {

	@Autowired
	private AttractionService attractionService;
	
	@GetMapping
	@ApiOperation(value = "관광지 목록 조회", notes = "페이지 번호, 관광지 타입, 검색어, 지역코드에 따른 관광지 목록을 조회한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageNum", value = "페이지 번호", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
		@ApiImplicitParam(name = "pageSize", value = "페이지 당 글 개수", required = true, dataType = "int", paramType = "query", defaultValue = "10"),
		@ApiImplicitParam(name = "type", value = "관광지 타입", required = true, dataType = "int", paramType = "query", defaultValue = "12"),
		@ApiImplicitParam(name = "keywords", value = "사용자 입력 검색어", required = false, dataType = "String", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "sido", value = "시/도 코드", required = false, dataType = "int", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "gugun", value = "구/군 코드", required = false, dataType = "int", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "curlatitude", value = "현위치 위도", required = false, dataType = "double", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "curlongitude", value = "현위치 경도", required = false, dataType = "double", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(name = "sort", value = "정렬 기준(distance, readcount, star, title 중 택1)", required = false, dataType = "String", paramType = "query", defaultValue = "readcount")
	})
	public ResponseEntity<PageInfo<Attraction>> getAttractionList(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam int type, @RequestParam(defaultValue = "") List<String> keywords, @RequestParam(defaultValue = "0") int sido, @RequestParam(defaultValue = "0") int gugun, @RequestParam(defaultValue = "0") double curlatitude, @RequestParam(defaultValue = "0") double curlongitude, @RequestParam(defaultValue = "readcount") String sort) throws SQLException {
		Map<String, Object> param = new HashMap<>();
		param.put("type", type);
		param.put("keywords", keywords);
		param.put("sido", sido);
		param.put("gugun", gugun);
		param.put("curlatitude", curlatitude);
		param.put("curlongitude", curlongitude);
		param.put("sort", sort);
		log.debug("getAttractionList에서 전달받은 파라미터: {}", param);
		PageHelper.startPage(pageNum, pageSize);
		return new ResponseEntity<PageInfo<Attraction>>(PageInfo.of(attractionService.getAttractionList(param)), HttpStatus.OK);
	}
	
	@GetMapping("/detail")
	@ApiOperation(value = "관광지 상세 조회", notes = "관광지 id를 통해 관광지를 상세 조회한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "attractionId", value = "관광지 id", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "userId", value = "사용자 id", required = true, dataType = "String", paramType = "query")
	})
	public ResponseEntity<?> getAttractionDetail(@RequestParam int attractionId, @RequestParam String userId) throws SQLException {
		Myplace param = new Myplace();
		param.setUserId(userId);
		param.setAttractionId(attractionId);
		Attraction myplace = attractionService.getMyplace(param);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("myplace", myplace != null ? true : false);
		log.debug("attractionController의 detail. db에서 가져온 myplace: {}", myplace);
		resultMap.put("attraction", attractionService.getAttractionDetail(attractionId));
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

}
