package com.loushi.utils.weibo;

import com.alibaba.fastjson.JSONObject;
import com.loushi.mapper.UserOrderCrawlCommentMapper;
import com.loushi.mapper.UserOrderCrawlForwardMapper;
import com.loushi.mapper.UserOrderCrawlLikeMapper;
import com.loushi.model.UserOrderCrawlComment;
import com.loushi.model.UserOrderCrawlForward;
import com.loushi.model.UserOrderCrawlLike;
import com.loushi.model.UserTask;
import com.loushi.util.StringUtil;
import com.loushi.utils.DateUtils;
import com.loushi.utils.commonPython.PythonHttpApi;
import com.loushi.vo.commonPython.WeiboCommentListVO;
import com.loushi.vo.commonPython.WeiboForwordVO;
import com.loushi.vo.commonPython.WeiboLikeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Slf4j
@Component
public class WeiboAuditUtil {


    @Resource
    private UserOrderCrawlCommentMapper commentMapper;
    @Resource
    private UserOrderCrawlLikeMapper likeMapper;
    @Resource
    private UserOrderCrawlForwardMapper forwardMapper;
    @Resource
    private PythonHttpApi pythonHttpApi;


    //============================  微博评论  ============================================

    public void weiboCommentData(UserTask task, String msgId, Integer page, Set<String> accountSet) {
        Object data = pythonHttpApi.getWeiboCommentList(msgId, page).getData();
        if (null == data) {
            log.info("没有【微博评论】数据了...{}", data);
        } else {

            WeiboCommentListVO weiboCommentListVO = JSONObject.parseObject(data.toString(), WeiboCommentListVO.class);
            List<WeiboCommentListVO.Comment> commentList = weiboCommentListVO.getCommentList();
            boolean nextPage = weiboCommentListVO.isNextPage();

            for (WeiboCommentListVO.Comment comment : commentList) {
                String commentId = String.valueOf(comment.getId());
                Date createdAt = DateUtils.weiboCommentStrToDate(comment.getPublish_time());
                String text = StringUtil.replaceMysqlCharacters(comment.getText());
                String user_id = String.valueOf(comment.getUser_id());
                String screen_name = StringUtil.replaceMysqlCharacters(comment.getScreen_name());

                //大于取消时间跳过
                if (createdAt.getTime() >= task.getCancelTime().getTime()) {
                    continue;
                }
                //爬取的终点时间就是发单的时间
                if (createdAt.getTime() < task.getPublishTime().getTime()) {
                    log.info("当前时间小于发单的时间，退出【微博评论】数据爬取...");
                    nextPage = false;
                    break;
                }
                //判断这个人是不是集合中的
                if (accountSet.contains(user_id)) {
                    UserOrderCrawlComment byMsgId = commentMapper.selectByMsgId(commentId);
                    if (byMsgId == null) {
                        UserOrderCrawlComment userOrderCrawlData = new UserOrderCrawlComment(null, task.getId(), commentId, createdAt, text, user_id, screen_name, new Date());
                        commentMapper.insertSelective(userOrderCrawlData);
                    }
                }
            }

            //下一页
            if (nextPage)
                weiboCommentData(task, msgId, weiboCommentListVO.getPage(), accountSet);
            else
                log.info("当前任务【{}】,【评论】数据爬取完毕了...", task.getId());
        }
    }


    public Set<String> weiboCommentData2(UserTask task, String msgId, int page, Set<String> newSet) {
        Object data = pythonHttpApi.getWeiboCommentList(msgId, page).getData();
        if (null == data) {
            log.info("没有【微博评论】数据了...{}", data);
        } else {

            WeiboCommentListVO weiboCommentListVO = JSONObject.parseObject(data.toString(), WeiboCommentListVO.class);
            List<WeiboCommentListVO.Comment> commentList = weiboCommentListVO.getCommentList();
            boolean nextPage = weiboCommentListVO.isNextPage();

            for (WeiboCommentListVO.Comment comment : commentList) {
                String commentId = String.valueOf(comment.getId());
                Date createdAt = DateUtils.weiboCommentStrToDate(comment.getPublish_time());

                //大于取消时间跳过
                if (createdAt.getTime() >= task.getCancelTime().getTime()) {
                    continue;
                }
                //爬取的终点时间就是发单的时间
                if (createdAt.getTime() < task.getPublishTime().getTime()) {
                    log.info("当前时间小于发单的时间，退出【微博评论】数据爬取...");
                    nextPage = false;
                    break;
                }
                newSet.add(commentId);
            }

            //下一页
            if (nextPage) {
                return weiboCommentData2(task, msgId, weiboCommentListVO.getPage(), newSet);
            } else {
                log.info("当前任务【{}】,【评论】数据爬取完毕了...", task.getId());
            }
        }
        return newSet;
    }


    //========================== 微博点赞==================================================================

    public void weiboLikeData(UserTask task, String msgId, Integer page, Set<String> accountSet) {
        Object data = pythonHttpApi.getWeiboLikeList(msgId, page).getData();
        if (null == data) {
            log.info("没有【微博点赞】数据了...{}", data);
        } else {

            WeiboLikeVO weiboLikeVO = JSONObject.parseObject(data.toString(), WeiboLikeVO.class);
            List<WeiboLikeVO.Like> likeList = weiboLikeVO.getLikeList();
            boolean nextPage = weiboLikeVO.isNextPage();

            for (WeiboLikeVO.Like like : likeList) {
                String likeId = String.valueOf(like.getId());
                Date createdAt = DateUtils.weiboCommentStrToDate(like.getCreated_at());
                String user_id = String.valueOf(like.getUid());
                String screen_name = StringUtil.replaceMysqlCharacters(like.getScreen_name());

                //大于取消时间跳过
                if (createdAt.getTime() >= task.getCancelTime().getTime()) {
                    continue;
                }
                //爬取的终点时间就是发单的时间
                if (createdAt.getTime() < task.getPublishTime().getTime()) {
                    log.info("当前时间小于发单的时间，退出【微博点赞】数据爬取...");
                    nextPage = false;
                    break;
                }
                //判断这个人是不是集合中的
                if (accountSet.contains(user_id)) {
                    UserOrderCrawlLike byMsgId = likeMapper.selectByMsgId(likeId);
                    if (byMsgId == null) {
                        UserOrderCrawlLike userOrderCrawlLike = new UserOrderCrawlLike(null, task.getId(), likeId, createdAt, user_id, screen_name, new Date());
                        likeMapper.insertSelective(userOrderCrawlLike);
                    }

                }
            }

            //下一页
            if (nextPage)
                weiboLikeData(task, msgId, weiboLikeVO.getPage(), accountSet);
            else
                log.info("当前任务【{}】,【点赞】数据爬取完毕了...", task.getId());
        }
    }


    public Set<String> weiboLikeData2(UserTask task, String msgId, int page, Set<String> newSet) {
        Object data = pythonHttpApi.getWeiboLikeList(msgId, page).getData();
        if (null == data) {
            log.info("没有【微博点赞】数据了...{}", data);
        } else {

            WeiboLikeVO weiboLikeVO = JSONObject.parseObject(data.toString(), WeiboLikeVO.class);
            List<WeiboLikeVO.Like> likeList = weiboLikeVO.getLikeList();
            boolean nextPage = weiboLikeVO.isNextPage();

            for (WeiboLikeVO.Like like : likeList) {
                String likeId = String.valueOf(like.getId());
                Date createdAt = DateUtils.weiboCommentStrToDate(like.getCreated_at());

                //大于取消时间跳过
                if (createdAt.getTime() >= task.getCancelTime().getTime()) {
                    continue;
                }
                //爬取的终点时间就是发单的时间
                if (createdAt.getTime() < task.getPublishTime().getTime()) {
                    log.info("当前时间小于发单的时间，退出【微博点赞】数据爬取...");
                    nextPage = false;
                    break;
                }
                newSet.add(likeId);
            }

            //下一页
            if (nextPage) {
                return weiboLikeData2(task, msgId, weiboLikeVO.getPage(), newSet);
            } else {
                log.info("当前任务【{}】,【点赞】数据爬取完毕了...", task.getId());
            }
        }
        return newSet;
    }


    //=================================  微博转发   ========================================================

    public void weiboForwordData(UserTask task, String msgId, Integer page, Set<String> accountSet) {
        Object data = pythonHttpApi.getWeiboForwordList(msgId, page).getData();
        if (null == data) {
            log.info("没有【微博转发】数据了...{}", data);
        } else {

            WeiboForwordVO weiboForwordVO = JSONObject.parseObject(data.toString(), WeiboForwordVO.class);
            List<WeiboForwordVO.Forword> forwords = weiboForwordVO.getForwordList();
            boolean nextPage = weiboForwordVO.isNextPage();

            for (WeiboForwordVO.Forword forword : forwords) {
                String forwordId = String.valueOf(forword.getId());
                Date createdAt = DateUtils.weiboCommentStrToDate(forword.getCreated_at());
                String user_id = String.valueOf(forword.getUid());
                String screen_name = StringUtil.replaceMysqlCharacters(forword.getScreen_name());

                //大于取消时间跳过
                if (createdAt.getTime() >= task.getCancelTime().getTime()) {
                    continue;
                }
                //爬取的终点时间就是发单的时间
                if (createdAt.getTime() < task.getPublishTime().getTime()) {
                    log.info("当前时间小于发单的时间，退出【微博转发】数据爬取...");
                    nextPage = false;
                    break;
                }
                //判断这个人是不是集合中的
                if (accountSet.contains(user_id)) {
                    UserOrderCrawlForward byMsgId = forwardMapper.selectByMsgId(forwordId);
                    if (byMsgId == null) {
                        UserOrderCrawlForward userOrderCrawlLike = new UserOrderCrawlForward(null, task.getId(), forwordId, createdAt, user_id, screen_name, new Date());
                        forwardMapper.insertSelective(userOrderCrawlLike);
                    }
                }
            }

            //下一页
            if (nextPage)
                weiboForwordData(task, msgId, weiboForwordVO.getPage(), accountSet);
            else
                log.info("当前任务【{}】,【转发】数据爬取完毕了...", task.getId());
        }
    }


    public Set<String> weiboForwordData2(UserTask task, String msgId, int page, Set<String> newSet) {
        Object data = pythonHttpApi.getWeiboForwordList(msgId, page).getData();
        if (null == data) {
            log.info("没有【微博转发】数据了...{}", data);
        } else {

            WeiboForwordVO weiboForwordVO = JSONObject.parseObject(data.toString(), WeiboForwordVO.class);
            List<WeiboForwordVO.Forword> forwords = weiboForwordVO.getForwordList();
            boolean nextPage = weiboForwordVO.isNextPage();

            for (WeiboForwordVO.Forword forword : forwords) {
                String forwordId = String.valueOf(forword.getId());
                Date createdAt = DateUtils.weiboCommentStrToDate(forword.getCreated_at());

                //大于取消时间跳过
                if (createdAt.getTime() >= task.getCancelTime().getTime()) {
                    continue;
                }
                //爬取的终点时间就是发单的时间
                if (createdAt.getTime() < task.getPublishTime().getTime()) {
                    log.info("当前时间小于发单的时间，退出【微博转发】数据爬取...");
                    nextPage = false;
                    break;
                }
                newSet.add(forwordId);
            }

            //下一页
            if (nextPage)
                return weiboForwordData2(task, msgId, weiboForwordVO.getPage(), newSet);
            else
                log.info("当前任务【{}】,【转发】数据爬取完毕了...", task.getId());
        }
        return newSet;
    }

}
