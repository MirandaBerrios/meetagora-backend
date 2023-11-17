package com.mirandez.meetagora.controller.webController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mirandez.meetagora.entity.report.ReportBySubject;
import com.mirandez.meetagora.entity.report.ReportByUser;
import com.mirandez.meetagora.mapper.OnBoardingMapper;
import com.mirandez.meetagora.services.ListRegistryService;
import com.mirandez.meetagora.services.ReportServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


@Controller
@Log4j2
public class PagesController {

    @Autowired
    OnBoardingMapper mapper;

    @Autowired
    ListRegistryService listRegistryService;

    @Autowired
    ReportServices reportServices;

    @Value("${dinamic.url}")
    String url;

    @Value("${server.servlet.context-path}")
    String context;

    @Value("${api.username}")
    String secretKey ;


    @RequestMapping(value="test", method = RequestMethod.GET)
    private String seeTest(Model model){
        return "test-see";
    }

    @RequestMapping(value="ajax", method = RequestMethod.GET)
    private String seeAjax(Model model){
        return "test-ajax";
    }

    @RequestMapping(value = "/validation/{token}", method = RequestMethod.GET)
    private String valiteByEmail(@PathVariable String token, Model model) {

        log.info("[ API ][ VALIDATE BY EMAIL ] Starting ");
        log.info("[ API ][ VALIDATE BY EMAIL ] Params:{} ", token);

        try {
            String tokenFromBbdd = mapper.selectTokenFromUser(token);
            if (token.equals(tokenFromBbdd)) {
                mapper.updateValidateByToken(token);

                log.info("[ API ][ VALIDATE BY EMAIL ] User was successfully validated");
                return "welcome";
            } else {

                log.info("[ API ][ VALIDATE BY EMAIL ] token is not validated ");

                return "not_found";
            }
        } catch (Exception e) {

            log.error("[ API ][ VALIDATE BY EMAIL ] Can't validated user by query");
            return "not_found";
        }

    }

    @GetMapping("/attendance/{token}")
    public ModelAndView generateQRCode(@PathVariable("token")String token)
                                        throws WriterException, IOException {

        ModelAndView modelAndView = new ModelAndView("asistencia-with-param");


        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // Utiliza la misma clave secreta
                .parseClaimsJws(token)
                .getBody();

        String name = (String) claims.get("subjectName");
        String classroom = (String) claims.get("sala");
        String section = (String) claims.get("subjectSection");


        QRCodeWriter barcodeWriter = new QRCodeWriter();
        String text= token;
        BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);

        modelAndView.addObject("qr", Base64.getEncoder().encodeToString(baos.toByteArray()));
        modelAndView.addObject("attendance", listRegistryService.getAllRegistryByTokenSession(token));
        modelAndView.addObject("token", token);
        modelAndView.addObject("name", name);
        modelAndView.addObject("classroom", classroom);
        modelAndView.addObject("section", section);
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView showLoginPage(){
        ModelAndView model = new ModelAndView("login_report");
        return model;
    }

    @GetMapping("/home_dashboard")
    public ModelAndView showDashboardPage() {
        ModelAndView model = new ModelAndView("report_home");
        List<ReportBySubject>  reportBySubjects =reportServices.getReportBySubject();
        List<ReportByUser> reportByUsers = reportServices.getReportByUser();

        model.addObject("reportSubject", reportBySubjects);
        model.addObject("reportUser", reportByUsers);
        return model;
    }
}
