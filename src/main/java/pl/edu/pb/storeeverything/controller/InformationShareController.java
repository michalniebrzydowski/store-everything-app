package pl.edu.pb.storeeverything.controller;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.edu.pb.storeeverything.dto.InformationEmailShareDto;
import pl.edu.pb.storeeverything.dto.InformationLinkShareDto;
import pl.edu.pb.storeeverything.dto.InformationShareDeleteDto;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.service.InformationShareService;

@Controller
@AllArgsConstructor
@RequestMapping("/information/share")
public class InformationShareController {

    private final InformationShareService informationShareService;

    private final Environment env;

    @GetMapping("/{informationId}")
    public String getShareForm(
            @AuthenticationPrincipal User user,
            @PathVariable Long informationId,
            Model model
    ) {
        model.addAttribute("linkShareRequest", new InformationLinkShareDto().withInformationId(informationId));
        model.addAttribute("emailShareRequest", new InformationEmailShareDto().withInformationId(informationId));
        model.addAttribute("deleteRequest", new InformationShareDeleteDto());
        model.addAttribute("linkShares", informationShareService.getLinkShares(user, informationId));
        model.addAttribute("emailShares", informationShareService.getEmailShares(user, informationId));
        model.addAttribute("appUrl", env.getProperty("app.url"));
        return "share-form";
    }

    @PostMapping("/link")
    public String shareByLink(
            @ModelAttribute("request") InformationLinkShareDto request,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        String link = informationShareService.shareByLink(user, request);
        redirectAttributes.addFlashAttribute("createdLink", link);
        return "redirect:/information/share/" + request.getInformationId();
    }

    @PostMapping("/email")
    public String shareByEmail(
            @ModelAttribute("request") InformationEmailShareDto request,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        try {
            informationShareService.shareByEmail(user, request);
        } catch (PersistenceException e) {
            redirectAttributes.addFlashAttribute("errorShareEmail", e.getMessage());
        }
        return "redirect:/information/share/" + request.getInformationId();
    }

    @DeleteMapping("/email/delete")
    public String unbindEmailShare(
            @AuthenticationPrincipal User sharedWith,
            @ModelAttribute("deleteRequest") InformationShareDeleteDto deleteRequest
    ) {
        informationShareService.unbindEmailShare(deleteRequest.withEmail(sharedWith.getEmail()));
        return "redirect:/information/shared";
    }

    @DeleteMapping("/{id}/email/delete")
    public String deleteEmailShare(
            @PathVariable Long id,
            @ModelAttribute("deleteRequest") InformationShareDeleteDto deleteRequest
    ) {
        informationShareService.deleteEmailShare(deleteRequest);

        return "redirect:/information/share/" + id;
    }

    @DeleteMapping("/link/delete/{informationId}/{url}")
    public String deleteLinkShare(
            @PathVariable Long informationId,
            @PathVariable String url,
            @AuthenticationPrincipal User owner
    ) {
        informationShareService.deleteLinkShare(owner, url);
        return "redirect:/information/share/" + informationId;
    }
}
