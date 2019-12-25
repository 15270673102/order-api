package com.loushi.service.weibo;

import com.loushi.vo.task.PublishTaskRabbitVO;
import com.loushi.vo.util.AgainCrawlVO;

public interface IWeiboCheck {

    void firstAuditWeiboTask(PublishTaskRabbitVO vo) throws Exception;

    void secondAuditWeiboTask(Integer taskId) throws Exception;

    void againCrawl(AgainCrawlVO vo);

}
