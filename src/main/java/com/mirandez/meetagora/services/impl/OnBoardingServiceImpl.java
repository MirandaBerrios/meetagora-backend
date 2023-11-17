package com.mirandez.meetagora.services.impl;

import com.mirandez.meetagora.entity.*;
import com.mirandez.meetagora.mapper.OnBoardingMapper;
import com.mirandez.meetagora.request.UserFormRequest;
import com.mirandez.meetagora.services.OnBoardingService;
import com.mirandez.meetagora.services.OnBoardingUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class OnBoardingServiceImpl implements OnBoardingService {

    @Autowired
    OnBoardingUtils onBoardingUtils;

    @Autowired
    OnBoardingMapper onBoardingMapper;


    @Override
    public Header extractHeaderFromPdf(File file) {
        Header headerResponse = new Header() ;
        try (PDDocument document = PDDocument.load(file)) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();

            while (pi.hasNext()) {
                // iterate over the pages of the document
                Page page = pi.next();

                List<TextElement> elements = page.getText();
                List<String> allText = new ArrayList<>();
                String[] buff = new String[elements.size()];

                for (int i = 0; i < elements.size(); i++) {
                    buff[i] = elements.get(i).getText();
                }

                String header = String.join("", buff);

                String fechaEmision = onBoardingUtils.extractByWords("emisión", "SEDE", header);
                String sede = onBoardingUtils.extractByWords("SEDE:", "N°", header);
                String numSede = onBoardingUtils.extractByWords("N°SEDE:", "BOLETIN", header);
                String nombre = onBoardingUtils.extractByWords("Alumno:", "Rut:", header);
                String rut = onBoardingUtils.extractByWords("Rut:", "Carrera:", header);
                String carrera = onBoardingUtils.extractByWords("Carrera:", "Jornada:", header);
                String jornada = onBoardingUtils.extractByWords("Jornada:", "Horario", header);
                String semestre = onBoardingUtils.extractByWords("ACADEMICA", "Alumno:", header);

                String[] nombreAux = nombre.replace(",", "").split(" ");
                nombre = nombreAux[1]+ " " + nombreAux[2];
                String apellido = nombreAux[0];

                String[] carreraAux = carrera.split("N°");
                carrera = carreraAux[0];
                String numCarrera = carreraAux[1];

                Date date1=new SimpleDateFormat("dd.MM.yyyy").parse(fechaEmision.trim());
                SimpleDateFormat sd = new SimpleDateFormat("dd MMM yyyy");
                fechaEmision = sd.format(date1);


                headerResponse.setFechaEmision(fechaEmision);
                headerResponse.setSede(sede);
                headerResponse.setNumSede(numSede) ;
                headerResponse.setNombre(nombre);
                headerResponse.setApellido(apellido);
                headerResponse.setRut(rut)  ;
                headerResponse.setCarrera(carrera);
                headerResponse.setJornada(jornada) ;
                headerResponse.setSemestre(semestre) ;
                headerResponse.setNumCarrera( numCarrera ) ;

            }
            return headerResponse;
        } catch(Exception e){
            log.error("[ ONBOARDING SERVICE IMPL ][ extractHeaderFromPdf ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ extractHeaderFromPdf ] Cause : {} ", e.getCause());
            log.error("[ ONBOARDING SERVICE IMPL ][ extractHeaderFromPdf ] Message : {} ", e.getMessage());
            return  null;
        }
    }

    @Override
    public User extractUserFromPdf(File file ) {
        User user = new User();

        try (PDDocument document = PDDocument.load(file)) {
            PageIterator pi = new ObjectExtractor(document).extract();

            while (pi.hasNext()) {

                Page page = pi.next();

                List<TextElement> elements = page.getText();
                List<String> allText = new ArrayList<>();
                String[] buff = new String[elements.size()];

                for (int i = 0; i < elements.size(); i++) {
                    buff[i] = elements.get(i).getText();
                }

                String header = String.join("", buff);

                String nombre = onBoardingUtils.extractByWords("Alumno:", "Rut:", header);
                String rut = onBoardingUtils.extractByWords("Rut:", "Carrera:", header);
                String carrera = onBoardingUtils.extractByWords("Carrera:", "Jornada:", header);


                String[] nombreAux = nombre.replace(",", "").split(" ");
                nombre = nombreAux[1]+ " " + nombreAux[2];
                String apellido = nombreAux[0];

                String[] carreraAux = carrera.split("N°");
                String numCarrera = carreraAux[1];

                user.setFirstName( nombre.split(" ")[0] );
                user.setRut( rut );
                user.setLastName( apellido );
                user.setSecondName( nombre.split(" ")[1] );
                user.setCareerId( Integer.parseInt( numCarrera ) );
            }
            return user;
        } catch(Exception e){
            log.error("[ ONBOARDING SERVICE IMPL ][ extractUserFromPdf ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ extractUserFromPdf ] Cause : {} ", e.getCause());
            log.error("[ ONBOARDING SERVICE IMPL ][ extractUserFromPdf ] Message : {} ", e.getMessage());
            return  null;
        }
    }



    @Override
    public Schedule extractScheduleFromPdf(File file , String url) {
//        jornada y url del bucket
        Schedule schedule = new Schedule();
        try (PDDocument document = PDDocument.load(file)) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();

            while (pi.hasNext()) {
                // iterate over the pages of the document
                Page page = pi.next();


                List<TextElement> elements = page.getText();
                List<String> allText = new ArrayList<>();
                String[] buff = new String[elements.size()];

                for (int i = 0; i < elements.size(); i++) {
                    buff[i] = elements.get(i).getText();
                }
                String header = String.join("", buff);

                String jornada = onBoardingUtils.extractByWords("Jornada:", "Horario", header);
                schedule.setScheduleType(jornada);
                schedule.setScheduleFile(url);

            }
            return  schedule;

        } catch(Exception e){
            log.error("[ ONBOARDING SERVICE IMPL ][ extractScheduleFromPdf ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ extractScheduleFromPdf ] Cause : {} ", e.getCause());
            log.error("[ ONBOARDING SERVICE IMPL ][ extractScheduleFromPdf ] Message : {} ", e.getMessage());
            return  null;
        }

    }

    @Override
    public List<Subject> extractSubjectListFromPdf(ArrayList<String> mainlList ) {
        try {
            List<Subject> subjectList = new ArrayList<>();
            mainlList.forEach(row -> {
                int some = 2+2;
                String[] column = row.split("%");

                for (int i = 0; i <= column.length - 1; i++) {
                    Subject subject = new Subject();
                    String horario = column[0];

                    if (horario.contains("A") && horario.length() > 7) {
                        try {

                            DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
                            DateTime dateTimeSTART = dtf.parseDateTime( horario.split("A")[0].trim() );
                            DateTime dateTimeEND = dtf.parseDateTime( horario.split("A")[1].trim() );
                            LocalTime localTimeSTART = dateTimeSTART.toLocalTime();
                            LocalTime localTimeEND = dateTimeEND.toLocalTime();
                            subject.setStartAt(localTimeSTART.toDateTimeToday().toDate());
                            subject.setEndAt(localTimeEND.toDateTimeToday().toDate());

                        }catch (Exception e){
                            System.err.println(e);

                        }
                    }


                    //			manejo del nombre codigo y sala
                    String first = "";
                    String classRoom = null;
                    String name = null;
                    String code = null;
                    String section = null;
                    Integer dayId = null;
                    if (column[i].contains("/")) {
                        String[] aux = column[i].split("/");
//                        if (aux.length >= 1 && aux[1] != null && !aux[1].isEmpty() && column[i].contains("/AO")) {
                        if (aux.length >= 1 ) {
                            first = aux[0];
                            try {
                                classRoom = aux[1].replace("AO-", "");
                            }catch (Exception e){

                                classRoom = "sin sala";
                            }

                            name = first.split("-")[0];
                            section = first.replace(name,"").replace("-","");
                            code = first.split("-")[0].substring( first.split("-")[0].length()-7, first.split("-")[0].length());
                            name = name.replace(code,"");
                            dayId = i;

                        } else {
                            classRoom = column[i];
                            dayId = i;
                        }

                        subject.setSubjectName(name.trim());
                        subject.setClassroomNumber(classRoom.trim());
                        subject.setDayId( dayId );
                        subject.setSubjectCode(code);
                        subject.setSubjectSection(section.trim());


                        subjectList.add(subject);
                    } else {
                        name = column[i];
                    }

                }

            });

            return subjectList;
        } catch (Exception e) {
            log.error("[ ONBOARDING SERVICE IMPL ][ convertMultipartToIoFile ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ convertMultipartToIoFile ] Cause : {} ", e.getCause().getMessage());
            log.error("[ ONBOARDING SERVICE IMPL ][ convertMultipartToIoFile ] Message : {} ", e.toString());
            return null;
        }
    }

    @Override
    public Career extractCareerFromPdf(File file ) {
        Career career = new Career();


        try (PDDocument document = PDDocument.load(file)) {
            PageIterator pi = new ObjectExtractor(document).extract();

            while (pi.hasNext()) {
                Page page = pi.next();
                List<TextElement> elements = page.getText();
                String[] buff = new String[elements.size()];
                for (int i = 0; i < elements.size(); i++) {
                    buff[i] = elements.get(i).getText();
                }
                String header = String.join("", buff);
                String carrera = onBoardingUtils.extractByWords("Carrera:", "Jornada:", header);
                String[] carreraAux = carrera.split("N°");
                carrera = carreraAux[0];
                String numCarrera = carreraAux[1];

                career.setCareerName( carrera );
                career.setCareerId( numCarrera );
            }
            return career;
        } catch(Exception e){
            log.error("[ ONBOARDING SERVICE IMPL ][ extractCareerFromPdf ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ extractCareerFromPdf ] Cause : {} ", e.getCause());
            log.error("[ ONBOARDING SERVICE IMPL ][ extractCareerFromPdf ] Message : {} ", e.getMessage());
            return  null;
        }
    }

    @Override
    public ArrayList<String> readFile(MultipartFile file) {

        try {
            File fl = new File(file.getOriginalFilename());
            fl.createNewFile();
            FileOutputStream fos = new FileOutputStream(fl);
            fos.write(file.getBytes());
            fos.close();

            ArrayList<String> someList = new ArrayList<>();
            String auxRow = "";
            String rowToInsert = "";
            String passRow = "" ;


            try (PDDocument document = PDDocument.load(fl)) {

                SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
                PageIterator pi = new ObjectExtractor(document).extract();
                while (pi.hasNext()) {
                    // iterate over the pages of the document
                    Page page = pi.next();
                    List<Table> table = sea.extract(page);
                    // iterate over the tables of the page
                    for (Table tables : table) {
                        List<List<RectangularTextContainer>> rows = tables.getRows();

                        for (List<RectangularTextContainer> cells : rows) {
                            String row = "";
                            // print all column-cells of the row plus linefeed
                            for (RectangularTextContainer content : cells) {
                                // Note: Cell.getText() uses \r to concat text chunks

                                String text = "";
                                if(!content.getText().isEmpty()) {
                                    text = content.getText().replace(" ", "#").replace("\r", " ");
                                }else {
                                    text = "#";
                                }

                                row = row.concat(text + "%");
                            }

                            if( row.substring(0,8).contains(" A ") && row.substring(0,8).indexOf(":") == 2) {
                                someList.add(row);

                            } else if ( !row.substring(0,8).contains("A ") &&  row.substring(0,8).contains(":") && row.substring(6,11).indexOf(":") !=10 && auxRow.isEmpty()) {
//							pass row to de next iteration
                                auxRow = row;

                            }else if(row.split("%")[0].equals("#") && !row.split("%")[0].contains("M")) {
//							set last row inserted to this iteration

                                passRow = someList.get(someList.size()-1);
                                String[] itemsRows = passRow.split("%");
                                for (int i = 0; i <= 6; i++) {
                                    if (!row.split("%")[i].contains("#")) {
                                        itemsRows[i] = itemsRows[i] + row.split("%")[i];
                                    }
                                }
                                someList.remove(someList.size()-1);
                                someList.add(String.join("%", itemsRows));

                            }else if ( row.substring(0,2).contains("A")  || !row.contains("Horario")){
                                auxRow = auxRow + row;
                                for( int i=0 ; i <=6 ; i++){
                                    rowToInsert = rowToInsert + auxRow.split("%")[i] + row.split("%")[i] + "%";
                                }
                                someList.add(rowToInsert);
                                auxRow = "";
                                rowToInsert= "";
                            }
                        }
                    }
                }

                ArrayList<String> cleanLIst = new ArrayList<>();
                someList.forEach(item -> {
                    String regex = "#";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(item);
                    cleanLIst.add(matcher.replaceAll(""));
//                    item.replace("#", "");
                });

                return cleanLIst;
            }catch (Exception e ){
                
                log.error("[ ONBOARDING SERVICE IMPL ][ READ FILE ]  Error ");
                log.error("[ ONBOARDING SERVICE IMPL ][ READ FILE ] Cause : {} ", e.getCause());
                log.error("[ ONBOARDING SERVICE IMPL ][ READ FILE ] Message : {} ", e.getMessage());
                return null;
            }
        } catch (IOException e) {
            log.error("[ ON-BOARDING ][ READ FILE ] Cause : {}  , Message : {}" , e.getCause(), e.getMessage());
            log.error("[ ON-BOARDING ][ READ FILE ] ERROR WHILE TRANSFORM FILE , IO EXCEPTION");
            return null;
        }

    }

    @Override
    public File convertMultipartToIoFile(MultipartFile file) {
        try {
            File fl = new File(file.getOriginalFilename());
            fl.createNewFile();
            FileOutputStream fos = new FileOutputStream(fl);
            fos.write(file.getBytes());
            fos.close();
            return fl;
        } catch (IOException e) {
            log.error("[ ONBOARDING SERVICE IMPL ][ convertMultipartToIoFile ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ convertMultipartToIoFile ] Cause : {} ", e.getCause());
            log.error("[ ONBOARDING SERVICE IMPL ][ convertMultipartToIoFile ] Message : {} ", e.getMessage());
            return null; 
        }
    }

    @Override
    public User completeUserWithFormParams(User user, UserFormRequest userFormRequest ,int idSchedule ) {
        try {
            user.setPassword(userFormRequest.getPassword());
            user.setInstitutionalEmail(userFormRequest.getInstitutionalEmail());
            if( user.getInstitutionalEmail().contains("@profesor.duoc.cl")){
                user.setIsTeacher("1");
            }else{
                user.setIsTeacher("0");
            }
            user.setNickname(userFormRequest.getNickName());
            user.setProfileImage(userFormRequest.getProfileImage());
            user.setPhoneNumber(userFormRequest.getPhoneNumber());
            user.setScheduleId(idSchedule);

            return user;
        }catch (Exception e){
            log.error("[ ONBOARDING SERVICE IMPL ][ completeUserWithFormParams ]  Error ");
            log.error("[ ONBOARDING SERVICE IMPL ][ completeUserWithFormParams ] Cause : {} ", e.getCause());
            log.error("[ ONBOARDING SERVICE IMPL ][ completeUserWithFormParams ] Message : {} ", e.getMessage());
            return null;

        }
    }

    @Override
    public boolean insertingInChain(User user, Schedule schedule, List<Subject> subject, Career career , List<Classroom> classroomList, List<Announcement> announcementList) {
        try {
            if(onBoardingMapper.registerUser(user) !=0){
                if(onBoardingMapper.registerSchedule(schedule) !=0){
                    if(onBoardingMapper.registerSubject(subject) !=0){
                        onBoardingMapper.insertallclassroom(classroomList);
                        onBoardingMapper.registerCareer(career);
//                        TODO crear maper para insertar anuncios

                        return false;


                    }
                }

            }
            return true;
        } catch (Exception e) {
            System.err.println(e);
            log.error("[******] Error : {}" ,e.toString());
            return true;
        }

    }

    @Override
    public Boolean verifyIfEmailExist(String email) {

        try {
            String response = onBoardingMapper.verifyIfExist(email);
            if( response.equalsIgnoreCase(email)  ){
                return true;
            }
            return false;
        } catch (Exception e) {
            log.info("[ VERIFY EMAIL ] USER DOESN'T EXISTE");
            return  false;
        }
    }

    @Override
    public Boolean insertSubject(Subject subject) {
        try {
            if(onBoardingMapper.insertSubject(subject)){
                return true;
            }
            log.error("[ SERVICE ] CAN'T INSERT SUBJECT");
            return false;
        }catch (Exception e){
            log.error("[ SERVICE ] FAIL ON INSERT");

            return false;
        }
    }


}
