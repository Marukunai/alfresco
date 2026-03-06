package com.someco.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("publishWebDelegate")
public class PublishWebDelegate implements JavaDelegate {

    @Autowired
    private NodeService nodeService;

    @Override
    public void execute(DelegateExecution execution) {
        try {
            String nodeRefStr = (String) execution.getVariable("nodeRef");
            String publishedBy = (String) execution.getVariable("initiator");

            NodeRef nodeRef = new NodeRef(nodeRefStr);

            Map<String, Serializable> props = new HashMap<>();
            props.put("{http://www.someco.com/model/ejercicio/1.0}publishedDate", new Date());
            props.put("{http://www.someco.com/model/ejercicio/1.0}publishedBy", publishedBy);

            nodeService.addAspect(nodeRef,
                    new org.alfresco.service.namespace.QName(
                            "http://www.someco.com/model/ejercicio/1.0",
                            "webPublished"
                    ), props);

            System.out.println("✅ Documento publicado a web: " + nodeRef);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}