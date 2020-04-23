package com.example.xiaoying.service;

import com.example.xiaoying.dto.NoteDTO;
import com.example.xiaoying.dto.PaginationDTO;
import com.example.xiaoying.dto.VideoDTO;
import com.example.xiaoying.mapper.NoteMapper;
import com.example.xiaoying.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: codeape
 * @Date: 2020/4/19 1:27
 * @Version: 1.0
 */
@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;


    public void insert(Note note) {
        noteMapper.insert(note);
    }

    public void createOrUpdate(Note note) {
        NoteExample noteExample = new NoteExample();
        noteExample.createCriteria()
                .andCreatorEqualTo(note.getCreator())
                .andVideoIdEqualTo(note.getVideoId());
        List<Note> notes = noteMapper.selectByExample(noteExample);
        if (notes.size()!=0){
            //更新笔记
            Note updateNote = new Note();
            updateNote.setId(notes.get(0).getId());
            updateNote.setContent(note.getContent());
            updateNote.setGmtModified(System.currentTimeMillis());
            noteMapper.updateByPrimaryKeySelective(updateNote);
        }else{
            //添加笔记
            noteMapper.insert(note);
        }
    }

    public Note queryNoteByVideoIdAndUser(String videoId, User user) {
        NoteExample noteExample = new NoteExample();
        noteExample.createCriteria()
                .andVideoIdEqualTo(Long.valueOf(videoId))
                .andCreatorEqualTo(Long.valueOf(user.getAccountId()));
        List<Note> notes = noteMapper.selectByExample(noteExample);
        if (notes.size()!=0){
            return notes.get(0);
        }
        return null;
    }

    public PaginationDTO queryMyNotes(String tag, Integer pageIndex, Integer size, User user) {
        List<NoteDTO> noteDTOS=new ArrayList();
        List<Note> notes=null;
        NoteExample myNoteExample = new NoteExample();
        myNoteExample.createCriteria()
                .andCreatorEqualTo(Long.valueOf(user.getAccountId()));
        myNoteExample.setOrderByClause("gmt_create desc");
        notes= noteMapper.selectByExample(myNoteExample);
        //获取video中的user
        for (Note note :
                notes) {
            NoteDTO noteDTO=new NoteDTO();
            BeanUtils.copyProperties(note,noteDTO);
            noteDTO.setUser(user);
            noteDTOS.add(noteDTO);
        }
        //排序后的数据分页
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalCount=noteDTOS.size();
        //计算总页数
        Integer totalPage;

        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=(totalCount/size)+1;
        }
        if (totalCount==0){
            totalPage=1;
        }

        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex<1){
            pageIndex=1;
        }else if(pageIndex>totalPage){
            pageIndex=totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);
        //将数据分页返回
        Integer offset=(pageIndex-1)*size;
        if (noteDTOS.size()-offset>size){
            noteDTOS=noteDTOS.subList(offset,offset+size);
        }else {
            noteDTOS=noteDTOS.subList(offset, noteDTOS.size());
        }

        paginationDTO.setData(noteDTOS);


        return paginationDTO;
    }

    public Note queryNoteById(Long id) {
        Note note = noteMapper.selectByPrimaryKey(id);
        return note;
    }
}
