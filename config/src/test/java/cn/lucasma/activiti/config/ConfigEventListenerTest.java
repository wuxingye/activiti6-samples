package cn.lucasma.activiti.config;

import cn.lucasma.activiti.event.CustomEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author Lucas Ma
 * <p>
 * EventListener 测试
 */
public class ConfigEventListenerTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigEventListenerTest.class);
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventlistener.cfg.xml");

    @Test
    @Deployment(resources = {"cn/lucasma/activiti/my-process.bpmn20.xml"})
    public void test() {
        // 打开 MDC
        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        assertEquals("Activiti is awesome!", task.getName());
        activitiRule.getTaskService().complete(task.getId());

        activitiRule.getRuntimeService().addEventListener(new CustomEventListener());
        activitiRule.getRuntimeService().dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));


//        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService()
//                .getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());
//
//        for (EventLogEntry eventLogEntry : eventLogEntries) {
//            logger.info("eventLog.type {}, eventLog.data {}", eventLogEntry.getType(),new String (eventLogEntry.getData()));
//        }
//
//        logger.info("eventLogEntries size ={}",eventLogEntries.size());
    }

}
