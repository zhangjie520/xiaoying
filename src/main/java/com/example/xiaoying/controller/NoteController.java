package com.example.xiaoying.controller;

import com.example.xiaoying.dto.FileDTO;
import com.example.xiaoying.dto.NoteCreateDTO;
import com.example.xiaoying.dto.ResultDTO;
import com.example.xiaoying.dto.VideoDTO;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.model.Note;
import com.example.xiaoying.model.User;
import com.example.xiaoying.provider.UcloudProvider;
import com.example.xiaoying.service.NoteService;
import com.example.xiaoying.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: codeape
 * @Date: 2020/4/18 22:35
 * @Version: 1.0
 */
@Controller
public class NoteController {
    @Autowired
    private UcloudProvider ucloudProvider;
    @Autowired
    private NoteService noteService;
    @Autowired
    private VideoService videoService;


    @PostMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String fileUrl = null;
        try {
            fileUrl = ucloudProvider.upload(file.getInputStream(),file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setMessage("上传成功");
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileUrl);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
    @PostMapping("/record")
    @ResponseBody
    public ResultDTO record(@RequestBody NoteCreateDTO noteDTO, HttpServletRequest request){
        if (noteDTO.getRecordContent()==null || "".equals(noteDTO.getRecordContent()) ){
            throw new CustomizeException(CustomizeErrorCode.NOTE_CONTENT_IS_EMPTY);
        }
        Note note=new Note();
        note.setVideoId(noteDTO.getVideoId());
        VideoDTO videoDTO = videoService.queryByVideoId(String.valueOf(noteDTO.getVideoId()));
        note.setTitle(videoDTO.getVideoTitle()+"笔记");
        note.setContent(noteDTO.getRecordContent());
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            ResultDTO resultDTO =ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
            return resultDTO;
        }
        note.setCreator(Long.valueOf(user.getAccountId()));
        note.setGmtCreate(System.currentTimeMillis());
        note.setGmtModified(System.currentTimeMillis());
        noteService.createOrUpdate(note);
        return ResultDTO.okOf(0);
    }
    @GetMapping("/note/{id}")
    public String note(@PathVariable("id") Long id,
                       Model model){
        Note note = noteService.queryNoteById(id);
        if (note==null){
            throw new CustomizeException(CustomizeErrorCode.NOTE_IS_EMPTY);
        }
        model.addAttribute("note",note);
        return "note";
    }
}
