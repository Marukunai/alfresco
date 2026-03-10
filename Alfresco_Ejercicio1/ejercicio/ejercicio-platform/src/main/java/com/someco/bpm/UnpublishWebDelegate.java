package com.someco.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("unpublishWebDelegate")
public class UnpublishWebDelegate implements JavaDelegate {
    @Autowired
    private NodeService nodeService;

    public static final String SC_NAMESPACE = "http://www.someco.com/model/ejercicio/1.0";

    @Override
    public void execute(DelegateExecution execution) {
        try {
            String nodeRefStr = (String) execution.getVariable("nodeRef");
            String initiator = (String) execution.getVariable("initiator");

            NodeRef nodeRef = new NodeRef(nodeRefStr);

            // Quitamos el aspecto de publicado
            QName aspectPublished = QName.createQName(SC_NAMESPACE, "webPublished");
            if (nodeService.hasAspect(nodeRef, aspectPublished)) {
                nodeService.removeAspect(nodeRef, aspectPublished);
            }

            // Si quieres registrar quién despublicó, usamos el otro aspecto
            Map<QName, Serializable> props = new HashMap<>();
            props.put(QName.createQName(SC_NAMESPACE, "unpublishedDate"), new Date());
            props.put(QName.createQName(SC_NAMESPACE, "unpublishedBy"), initiator);

            nodeService.addAspect(nodeRef, QName.createQName(SC_NAMESPACE, "webUnpublished"), props);

            System.out.println("Documento DESPUBLICADO de la web: " + nodeRef);

        } catch (Exception e) {
            System.err.println("Error en UnpublishWebDelegate:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
