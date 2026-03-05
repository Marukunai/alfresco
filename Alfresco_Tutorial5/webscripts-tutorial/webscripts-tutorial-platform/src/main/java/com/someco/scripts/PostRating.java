package com.someco.scripts;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.someco.beans.RatingBean;

/**
 * This is the controller for the rating.post web script.
 *
 * @author jpotts
 *
 */
public class PostRating extends DeclarativeWebScript {

    Logger logger = Logger.getLogger(PostRating.class);

    private RatingBean ratingBean;
    private NodeService nodeService;

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req,
                                              Status status) {
        String id = req.getParameter("id");
        String rating = req.getParameter("rating");
        String user = req.getParameter("user");

        // before: || rating.equals("0")
        if (id == null || rating == null || user == null) {
            logger.debug("ID, rating, or user not set");
            status.setCode(400, "Required data has not been provided");
            status.setRedirect(true);
        // NEW: check if rating != 0
        } else if (rating.equals("0")) {
            logger.debug("Rating can't be 0");
            status.setCode(400, "The rating introduced cannot be 0. Try using a number between 1-5");
            status.setRedirect(true);
        } else {
            logger.debug("Getting current node");
            NodeRef curNode = new NodeRef("workspace://SpacesStore/" + id);
            if (!nodeService.exists(curNode)) {
                logger.debug("Node not found");
                status.setCode(404, "No node found for id:" + id);
                status.setRedirect(true);
            } else {
                ratingBean.create(curNode, Integer.parseInt(rating), user);
                logger.debug("Back from ratingBean.create()");
            }

        }

        logger.debug("Setting model");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("node", id);
        model.put("rating", rating);
        model.put("user", user);

        return model;
    }

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public RatingBean getRatingBean() {
        return ratingBean;
    }

    public void setRatingBean(RatingBean ratingBean) {
        this.ratingBean = ratingBean;
    }

}