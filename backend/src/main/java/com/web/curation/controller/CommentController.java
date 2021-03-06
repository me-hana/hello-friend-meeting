package com.web.curation.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.entity.Alarm;
import com.web.curation.model.entity.Board;
import com.web.curation.model.entity.Comment;
import com.web.curation.model.entity.UserInfo;
import com.web.curation.model.repository.AlarmRepository;
import com.web.curation.model.repository.BoardRepository;
import com.web.curation.model.repository.CommentRepository;
import com.web.curation.model.repository.GroupInfoRepository;
import com.web.curation.model.repository.UserInfoRepository;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class CommentController {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	GroupInfoRepository groupInfoRepository;
	
	@PostMapping("/getCommentList")
	public Object getCommentList(@RequestParam int bno, @RequestParam String email) {
		Map<String, Object> resultMap=new HashMap<>();
		List<Comment> list=commentRepository.findAllByBno(bno);
		
		List<String> writerList=new ArrayList<>();
		List<Boolean> isWriterList=new ArrayList<>();
		for(Comment comment:list) {
			writerList.add(userInfoRepository.findById(comment.getCwriter()).get().getUname());
			if(userInfoRepository.findById(comment.getCwriter()).get().getEmail().equals(email))
				isWriterList.add(true);
			else
				isWriterList.add(false);
		}
		
		resultMap.put("isWriterList",isWriterList);
		resultMap.put("comments",list);
		resultMap.put("writerList", writerList);
		return resultMap;
	}
	
	@PostMapping("/writeComment")
	public Object writeComment(@RequestParam String email, @RequestParam String ccontent, @RequestParam int bno) {
		Map<String, Object> resultMap=new HashMap<>();
		
		UserInfo myInfo=userInfoRepository.findByEmail(email);
		
		Board board=boardRepository.findById(bno).get();
		UserInfo alarmTargetInfo=userInfoRepository.findById(board.getBwriter()).get();
		
		Comment comment=new Comment();
		comment.setBno(bno);
		comment.setCcontent(ccontent);
		comment.setCwriter(myInfo.getUno());
		commentRepository.save(comment);

		resultMap.put("data","?????? ????????? ??????????????????.");
		
		if(alarmTargetInfo.getEmail().equals(email)) 
			return resultMap;
			
		
		Alarm alarm=new Alarm();
		alarm.setAurl("BoardDetail");
		alarm.setCreateUser(myInfo.getUno());
		alarm.setAurlNo(bno);
		alarm.setAtype(0);
		alarm.setAuser(alarmTargetInfo.getUno());
		
		StringBuilder sb=new StringBuilder();
		sb.append(myInfo.getUname());
		sb.append("?????? ");
		sb.append(groupInfoRepository.findById(board.getBgno()).get().getGname());
		sb.append("????????? ");
		sb.append(board.getBtitle());
		sb.append("?????? ????????? ???????????????.");
		alarm.setAsummary(sb.toString());
		
		alarmRepository.save(alarm);
		
		
		return resultMap;
	}

	@PostMapping("/modifyComment")
	public Object modifyComment(@RequestParam int cno, @RequestParam String ccontent) {
		Map<String, Object> resultMap=new HashMap<>();
		Comment comment=commentRepository.findById(cno).get();
		comment.setCcontent(ccontent);
		
		commentRepository.save(comment);
		resultMap.put("data","?????? ????????? ??????????????????.");
		
		return resultMap;
	}
	
	@PostMapping("/delComment")
	public Object delComment(@RequestParam int cno) {
		Map<String, Object> resultMap=new HashMap<>();
		
		Comment comment=commentRepository.findById(cno).get();
		commentRepository.delete(comment);
		resultMap.put("data","?????? ????????? ??????????????????.");
		
		return resultMap;
	}
}
