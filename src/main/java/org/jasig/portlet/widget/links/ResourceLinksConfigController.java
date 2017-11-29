package org.jasig.portlet.widget.links;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@Slf4j
@RequestMapping("CONFIG")
public class ResourceLinksConfigController extends ResourceLinksBaseController {

    @RenderMapping
    public String config(final PortletRequest req) throws Exception {
        return "links/configLinks";
    }

    @ActionMapping
    public void saveResourceLinks(ActionRequest req, ActionResponse res,
                                  @RequestParam(value="save", required = false) String save,
                                  @RequestParam(value="links", required = false) String linksJson) throws PortletException, IOException {

        // validate JSON of links by converting to list of ResouceLink objects
        if (StringUtils.isNotBlank(save)) {
            log.debug(linksJson);
            List<ResourceLink> links = ResourceLinkService.jsonArrayToLinkList(linksJson);
            if (links == null || links.isEmpty()) {
                return;  // send them back to config mode
            }
            String[] validLinksJson = ResourceLinkService.linkListToJsonStrArray(links);
            // save  to preferences
            req.getPreferences().setValues(PREF_LINK_ATTR, validLinksJson);
            req.getPreferences().store();
        }
        res.setPortletMode(PortletMode.VIEW);
    }

    @ModelAttribute("linksJson")
    public String getResourceLinksJsonArray(PortletRequest req) {
        return super.getResourceLinksJson(req);
    }

    @ModelAttribute("groups")
    public Set<String> getGroups() {
        return super.getGroups();
    }

    @ModelAttribute("iconSizePixels")
    public String getIconSizePixels(PortletRequest req) {
        return super.getIconSizePixels(req);
    }
}
