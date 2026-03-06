package com.someco.bpm;

import org.alfresco.repo.workflow.activiti.ActivitiScriptNode;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.workflow.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Tarea de workflow que aplica el aspecto de publicación web
 */
@Component
public class ApplyWebPublishedAspect {

    @Autowired
    private NodeService nodeService;

    /**
     * Aplica el aspecto webPublished al documento
     */
    public void applyPublishedAspect(ActivitiScriptNode scriptNode, String publishedBy) {
        try {
            NodeRef nodeRef = scriptNode.getNodeRef();

            // Crear propiedades del aspecto
            Map<String, Serializable> properties = new HashMap<>();
            properties.put("{http://www.someco.com/model/ejercicio/1.0}publishedDate", new Date());
            properties.put("{http://www.someco.com/model/ejercicio/1.0}publishedBy", publishedBy);

            // Aplicar aspecto
            nodeService.addAspect(nodeRef,
                    new org.alfresco.service.namespace.QName(
                            "http://www.someco.com/model/ejercicio/1.0",
                            "webPublished"
                    ),
                    properties);

            System.out.println("Aspecto webPublished aplicado a: " + nodeRef);

        } catch (Exception e) {
            throw new WorkflowException("Error aplicando aspecto webPublished: " + e.getMessage(), e);
        }
    }

    /**
     * Aplica el aspecto webUnpublished al documento
     */
    public void applyUnpublishedAspect(ActivitiScriptNode scriptNode, String unpublishedBy) {
        try {
            NodeRef nodeRef = scriptNode.getNodeRef();

            // Crear propiedades del aspecto
            Map<String, Serializable> properties = new HashMap<>();
            properties.put("{http://www.someco.com/model/ejercicio/1.0}unpublishedDate", new Date());
            properties.put("{http://www.someco.com/model/ejercicio/1.0}unpublishedBy", unpublishedBy);

            // Aplicar aspecto
            nodeService.addAspect(nodeRef,
                    new org.alfresco.service.namespace.QName(
                            "http://www.someco.com/model/ejercicio/1.0",
                            "webUnpublished"
                    ),
                    properties);

            System.out.println("Aspecto webUnpublished aplicado a: " + nodeRef);

        } catch (Exception e) {
            throw new WorkflowException("Error aplicando aspecto webUnpublished: " + e.getMessage(), e);
        }
    }
}