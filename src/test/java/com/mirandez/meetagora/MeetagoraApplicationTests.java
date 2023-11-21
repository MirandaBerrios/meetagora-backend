package com.mirandez.meetagora;

import com.mirandez.meetagora.entity.Subject;
import com.mirandez.meetagora.services.impl.OnBoardingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MeetagoraApplicationTests {
//
//	@Test
//	void contextLoads() throws IOException {
//		File fl = new File("src/main/resources/horario.pdf");
//
//		ArrayList<String> someList = new ArrayList<>();
//		try (PDDocument document = PDDocument.load(fl)) {
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
//				List<Table> table = sea.extract(page);
//				// iterate over the tables of the page
//				for (Table tables : table) {
//					List<List<RectangularTextContainer>> rows = tables.getRows();
//					// iterate over the rows of the table
//					for (List<RectangularTextContainer> cells : rows) {
//						String row = "";
//						// print all column-cells of the row plus linefeed
//						for (RectangularTextContainer content : cells) {
//							// Note: Cell.getText() uses \r to concat text chunks
//							String text = content.getText().replace("\r", " ");
//							System.out.print(text + "%");
//							row = row.concat(text + "%");
//
//						}
//						System.out.println();
//						someList.add(row);
//					}
//				}
//			}
//		}
//		ObjectMapper om = new ObjectMapper();
//
//		ArrayList<String> cleanList = new ArrayList<>(someList); // Copia de la lista original
//
//		Iterator<String> iterator = cleanList.iterator();
//		int toRemove = 0 ;
//		while (iterator.hasNext()) {
//			try {
//				String item = iterator.next();
//
//				String horario = item.substring(0, 8);
//				if (horario.contains("%")) {
//					toRemove = someList.indexOf(item)-2;
//				}
//
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		cleanList.remove(toRemove);
//
//		List<Subject> schedule = new ArrayList<>();
//		AtomicInteger counter = new AtomicInteger();
//		counter.set(0);
//		AtomicInteger index = new AtomicInteger();
//		index.set(0);
//		Map<Integer, String> miMapa = new HashMap<>();
//
//		cleanList.forEach(block -> {
//
//			String[] item = block.split("%");
//			String horario = item[0];
//
//			String startAt = "";
//			String endAt = "";
//			String day = "";
//
//			if (counter.getAndIncrement() < 1) {
//				for (int i = 1; i <= 6; i++) {
//					miMapa.put(i, item[i]);
//					counter.getAndIncrement();
//				}
//				System.err.println(miMapa);
//			}
//
//
////			manejo del horario
//			if (horario.contains("A") && horario.length() > 7) {
//				startAt = horario.split("A")[0];
//				endAt = horario.split("A")[1];
//				horario = startAt + " -- " + endAt;
//			}
//			String second = null;
//			String name = null;
//			String code = null;
//
//
////			manejo del nombre codigo y sala
//			String mondayCalss = item[1];
//			String first = "";
//			second = "sin sala";
//			name = "";
//			code = "";
//			if (mondayCalss.contains("/AO")) {
//				String[] aux = mondayCalss.split("/");
//				if (aux.length >= 2) {
//					first = aux[0];
//					second = aux[1].split("-")[1];
//					code = first.split(" ")[first.split(" ").length - 1];
//					name = first.replace(code, "");
//				} else {
//					first = aux[0];
//				}
//			}
//			if (name != null && !name.isEmpty()) {
//				day = miMapa.get(1);
//				System.err.println("day: " + day + " start: " + startAt + " ends: " + endAt + " name: " + name + " code: " + code + " classroom: " + second);
//			}
//
//
//		});
//	}
//
//
//	@Test
//	void scrapingPdf() throws IOException {
//		File fl = new File("src/main/resources/horario.pdf");
//
//		ArrayList<String> someList = new ArrayList<>();
//		try (PDDocument document = PDDocument.load(fl)) {
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
//				List<Table> table = sea.extract(page);
//				// iterate over the tables of the page
//				for (Table tables : table) {
//					List<List<RectangularTextContainer>> rows = tables.getRows();
//					// iterate over the rows of the table
//					for (List<RectangularTextContainer> cells : rows) {
//						String row = "";
//						// print all column-cells of the row plus linefeed
//						for (RectangularTextContainer content : cells) {
//							// Note: Cell.getText() uses \r to concat text chunks
//							String text = content.getText().replace("\r", " ");
//							row = row.concat(text + "%");
//
//						}
//						someList.add(row);
//					}
//				}
//			}
//		}
//
////		TODO importación para soportar localTime
////		ObjectMapper om = new ObjectMapper();
////		om.registerModule(new JavaTimeModule());
//
////		TODO importación para soportar jodaTime
//		ObjectMapper om = new ObjectMapper();
//		om.registerModule(new JodaModule());
//
//
//		ArrayList<String> cleanList = new ArrayList<>(someList); // Copia de la lista original
//
//		Iterator<String> iterator = cleanList.iterator();
//		int toRemove = 0 ;
//		while (iterator.hasNext()) {
//			try {
//				String item = iterator.next();
//				String horario = item.substring(0, 8);
//				if (horario.contains("%")) {
//					toRemove = someList.indexOf(item)-1;
//				}
//
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		;
//		System.err.println(cleanList.remove(toRemove));
//		System.err.println("");
//
//		List<Subject> subjects = new ArrayList<>();
//		AtomicInteger rowCounter = new AtomicInteger();
//		rowCounter.set(0);
//		cleanList.forEach(row -> {
//
////			System.err.println(rowIndex+ "#  "+  row);
//
//			String[] column = row.split("%");
//
//			for (int i = 0; i <= column.length - 1; i++) {
//				Subject subject = new Subject();
//				String horario = column[0];
//				int startAtHour ;
//				int startAtMinute ;
//				int endAtHour ;
//				int endAtMinute ;
//				if (horario.contains("A") && horario.length() > 7) {
//					try {
//						startAtHour = Integer.parseInt(horario.split("A")[0].trim().split(":")[0]);
//						startAtMinute = Integer.parseInt(horario.split("A")[0].trim().split(":")[1]);
//						endAtHour = Integer.parseInt(horario.split("A")[1].trim().split(":")[0]);
//						endAtMinute = Integer.parseInt(horario.split("A")[1].trim().split(":")[1]);
////						subject.setStartAt(new LocalTime(startAtHour,startAtMinute).toDateTimeToday().toDate());
////						subject.setEndAt(new LocalTime(endAtHour,endAtMinute).toDateTimeToday().toDate());
//						DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:MM");
//						DateTime dateTimeSTART = dtf.parseDateTime( horario.split("A")[0].trim() );
//						DateTime dateTimeEND = dtf.parseDateTime( horario.split("A")[1].trim() );
//						LocalTime localTimeSTART = dateTimeSTART.toLocalTime();
//						LocalTime localTimeEND = dateTimeEND.toLocalTime();
//						subject.setStartAt(localTimeSTART.toDateTimeToday().toDate());
//						subject.setEndAt(localTimeEND.toDateTimeToday().toDate());
//
//
//					}catch (Exception e){
//						System.err.println(e);
//					}
//				}
//
//				//			manejo del nombre codigo y sala
//				String first = "";
//				String classRoom = null;
//				String name = null;
//				String code = null;
//				if (column[i].contains("/AO")) {
//					String[] aux = column[i].split("/");
//					if (aux.length >= 2 && aux[1] != null && !aux[1].isEmpty()) {
//						first = aux[0];
//						classRoom = aux[1].split("AO")[1].replace("-","").trim();
//						code = first.split(" ")[first.split(" ").length - 1];
//						name = first.replace(code, "");
//						subject.setClassroomNumber((first.split(" ")[first.split(" ").length - 1]));
//						subject.setSubjectName(name);
//						subject.setClassroomNumber(classRoom);
//						subject.setDayId(i);
//					} else {
//						first = aux[0];
//					}
//
//					subjects.add(subject);
//				}
//
//			}
//
//		});
//
////		System.err.println(om.writeValueAsString(subjects));
//
//
//	}
//
//	@Test
//	public void extractHeaders() {
//		File fl = new File("src/main/resources/horario_2023_2.pdf");
//		ObjectMapper om = new ObjectMapper();
//		ArrayList<String> someList = new ArrayList<>();
//		User person = new User();
//
//
//
//		try (PDDocument document = PDDocument.load(fl)) {
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
////				System.out.println(new ObjectMapper().writeValueAsString(page.getText().get(0).getText()));
//
//				List<TextElement> elements = page.getText();
//				List<String> allText = new ArrayList<>();
//				String[] buff = new String[elements.size()];
//
//				for (int i = 0; i < elements.size(); i++) {
//					buff[i] = elements.get(i).getText();
//				}
//
//				String header = String.join("", buff);
//
//				String fechaEmision = extractByWords("emisión", "SEDE", header);
//				String sede = extractByWords("SEDE:", "N°", header);
//				String numSede = extractByWords("N°SEDE:", "BOLETIN", header);
//				String nombre = extractByWords("Alumno:", "Rut:", header);
//				String rut = extractByWords("Rut:", "Carrera:", header);
//				String carrera = extractByWords("Carrera:", "Jornada:", header);
//				String jornada = extractByWords("Jornada:", "Horario", header);
//				String semestre = extractByWords("ACADEMICA", "Alumno:", header);
//
//				String[] nombreAux = nombre.replace(",", "").split(" ");
//				nombre = nombreAux[1]+ " " + nombreAux[2];
//				String apellido = nombreAux[0];
//
//				String[] carreraAux = carrera.split("N°");
//				carrera = carreraAux[0];
//				String numCarrera = carreraAux[1];
//
//				Date date1=new SimpleDateFormat("dd.MM.yyyy").parse(fechaEmision.trim());
//				SimpleDateFormat sd = new SimpleDateFormat("dd MMM yyyy");
//				fechaEmision = sd.format(date1);
//
//
//				System.out.println( fechaEmision ) ;
//				System.out.println( sede ) ;
//				System.out.println( numSede ) ;
//				System.out.println( nombre ) ;
//				System.out.println( rut ) ;
//				System.out.println( carrera ) ;
//				System.out.println( jornada ) ;
//				System.out.println( semestre ) ;
//
////				System.out.println(new ObjectMapper().writeValueAsString(person));
//			}
//			} catch(Exception e){
//				System.err.println(e.getMessage());
//			}
//		}
//		public String extractByWords(String word1, String word2, String text){
//			int start = text.indexOf(word1)+word1.length();
//			int stop = text.indexOf(word2);
//			String out ="";
//			for (int i = start; i < stop; i++) {
//				out = out.concat(String.valueOf(text.charAt(i)));
//			}
//		return out;
//	}
//
//	@Test
//	public void fixList() throws IOException {
//		File fl = new File("src/main/resources/horario_2021_1.pdf");
//
//		ArrayList<String> someList = new ArrayList<>();
//		try (PDDocument document = PDDocument.load(fl)) {
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
//				List<Table> table = sea.extract(page);
//				// iterate over the tables of the page
//				for (Table tables : table) {
//					List<List<RectangularTextContainer>> rows = tables.getRows();
//					// iterate over the rows of the table
//					for (List<RectangularTextContainer> cells : rows) {
//
//						String row = "";
//						// print all column-cells of the row plus linefeed
//						for (RectangularTextContainer content : cells) {
//							// Note: Cell.getText() uses \r to concat text chunks
//							String text = content.getText().replace("\r", " ");
//							row = row.concat(text + "%");
//
//						}
//						someList.add(row);
//					}
//				}
//			}
//		}
//
//
//
////		TODO importación para soportar jodaTime
//		ObjectMapper om = new ObjectMapper();
//		om.registerModule(new JodaModule());
//
//
//		ArrayList<String> cleanList = new ArrayList<>(someList); // Copia de la lista original
//
//		Iterator<String> iterator = cleanList.iterator();
//		int toRemove = 0 ;
//		while (iterator.hasNext()) {
//			try {
//				String item = iterator.next();
//				String horario = item.substring(0, 8);
//				if (horario.contains("%")) {
//					toRemove = someList.indexOf(item)-1;
//				}
//
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		cleanList.remove(toRemove);
//		List<String[]> tempCell = new ArrayList<>();
//		System.out.println(cleanList);
//		someList.forEach( item -> {
//			String[] cell = item.replace("%%","%-%").split("%");
//			if( cell[0].length() < 8 ){
//				tempCell.add(cell);
//			}
//		});
//
//		final String[] horario = {""};
//		final String[] lunes = {""};
//		final String[] martes = {""};
//		final String[] miercoles = {""};
//		final String[] jueves = {""};
//		final String[] viernes = {""};
//		final String[] sabado = {""};
//		tempCell.forEach( item -> {
//			horario[0] = horario[0] + " " + item[0];
//			lunes[0] = lunes[0] + item[1];
//			martes[0] = martes[0] + item[2];
//			miercoles[0] = miercoles[0] + item[3];
//			jueves[0] = jueves[0] + item[4];
//			viernes[0] = viernes[0] + item[5];
//		});
//
//		System.out.println(horario[0] + "%"+  lunes[0] + "%"+ martes[0] + "%"+miercoles[0] + "%"+jueves[0] +"%"+ viernes[0] +"%%" );
//
//	}
//
//
////	TODO DONE! solo falta concatenar cuando sea necesario
//	@Test
//	public void solution2Page() throws IOException {
//
//		File fl = new File("src/main/resources/horario.pdf");
//
//		ArrayList<String> someList = new ArrayList<>();
//		try (PDDocument document = PDDocument.load(fl)) {
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
//				List<Table> table = sea.extract(page);
//				// iterate over the tables of the page
//				for (Table tables : table) {
//					List<List<RectangularTextContainer>> rows = tables.getRows();
//					// iterate over the rows of the table
//					for (List<RectangularTextContainer> cells : rows) {
//						String row = "";
//						// print all column-cells of the row plus linefeed
//						for (RectangularTextContainer content : cells) {
//							// Note: Cell.getText() uses \r to concat text chunks
//							String text = content.getText().replace("\r", " ");
//							System.out.print(text + "%");
//							row = row.concat(text + "%");
//
//						}
//						System.out.println();
//						someList.add(row);
//					}
//				}
//			}
//		}
//		ObjectMapper om = new ObjectMapper();
//		// Agregar elementos a someList
//
//		ArrayList<String> cleanList = new ArrayList<>(someList); // Copia de la lista original
//
//		Iterator<String> iterator = cleanList.iterator();
//		int toRemove = 0 ;
//		while (iterator.hasNext()) {
//			try {
//				String item = iterator.next();
//
//				String horario = item.substring(0, 8);
//				if (horario.contains("%")) {
//					toRemove = someList.indexOf(item)-1;
//
//				}
//
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		cleanList.remove(toRemove);
//
//		List<String> auxList = new ArrayList<>();
//
//		AtomicInteger index = new AtomicInteger();
//		index.set(0);
//		List<Integer> toRemoveList = new ArrayList<>();
//
//		cleanList.forEach( item->{
//			index.getAndIncrement();//0
//			String[] columnas ;
//			columnas = item.split("%");
//
//			if(columnas[0].length() < 8){
//				toRemoveList.add(index.get());
//				auxList.add(item);
//			}
//		});
//
//		toRemoveList.forEach( value -> {
//			cleanList.remove(value);
//		});
//
//		String test = "";
//		String test1 = "";
//		String test2 = "";
//		String test3 = "";
//		String test4 = "";
//		String test5 = "";
//		String test6 = "";
//
//
//
//		List<String> auxListFinal = new ArrayList<>();
//
//		for(int i=0 ; i< auxList.size(); i++){
//			String[] col = auxList.get(i).split("%");
//			if(col.length == 6) {
//				test = test + col[0];
//				test1 = test1 + col[1];
//				test2 = test2 + col[2];
//				test3 = test3 + col[3];
//				test4 = test4 + col[4];
//				test5 = test5 + col[5];
//			}else if( col.length == 7){
//				test = test + col[0];
//				test1 = test1 + col[1];
//				test2 = test2 + col[2];
//				test3 = test3 + col[3];
//				test4 = test4 + col[4];
//				test5 = test5 + col[5];
//				test6 = test6 + col[6];
//			}
//		}
//		cleanList.add(test + "%" +test1 + "%" +test2 + "%" +test3 + "%" +test4 + "%" +test5 + "%" +test6 + "%" );
//
//		System.err.println(om.writeValueAsString(cleanList));
//
//
//
//
//
//	}
//
//
//	@Test
//	void backToStart(){
//		File fl = new File("C:/Users/jocpa/OneDrive/Documents/A test/test.pdf");
////		File fl = new File("src/main/resources/horario_2021_1.pdf");
//
//		ArrayList<String> someList = new ArrayList<>();
//		String auxRow= "";
//		String rowToInsert="";
//		String passRow = "";
//
//
//		try (PDDocument document = PDDocument.load(fl)) {
//			ObjectMapper om = new ObjectMapper();
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
//				List<Table> table = sea.extract(page);
//				// iterate over the tables of the page
//				for (Table tables : table) {
//					List<List<RectangularTextContainer>> rows = tables.getRows();
//
//					for (List<RectangularTextContainer> cells : rows) {
//						String row = "";
//						// print all column-cells of the row plus linefeed
//						for (RectangularTextContainer content : cells) {
//							// Note: Cell.getText() uses \r to concat text chunks
//
//							String text = "";
//							if(!content.getText().isEmpty()) {
//								text = content.getText().replace(" ", "#").replace("\r", " ");
//							}else {
//								text = "#";
//							}
//
//							row = row.concat(text + "%");
//						}
//
//						System.err.println(row);
//
//						if( row.substring(0,8).contains(" A ") && row.substring(0,8).indexOf(":") == 2) {
//							someList.add(row);
//
//						} else if ( !row.substring(0,8).contains("A ") &&  row.substring(0,8).contains(":") && row.substring(6,11).indexOf(":") !=10 && auxRow.isEmpty()) {
////							pass row to de next iteration
//							auxRow = row;
//
//						}else if(row.split("%")[0].equals("#") && !row.split("%")[0].contains("M")) {
////							set last row inserted to this iteration
//
//							passRow = someList.get(someList.size()-1);
//							String[] itemsRows = passRow.split("%");
//							for (int i = 0; i <= 6; i++) {
//								if (!row.split("%")[i].contains("#")) {
//									itemsRows[i] = itemsRows[i] + row.split("%")[i];
//								}
//							}
//							someList.remove(someList.size()-1);
//							someList.add(String.join("%", itemsRows));
//
//						}else if ( row.substring(0,2).contains("A")  || !row.contains("Horario")){
//							auxRow = auxRow + row;
//							for( int i=0 ; i <=6 ; i++){
//								rowToInsert = rowToInsert + auxRow.split("%")[i] + row.split("%")[i] + "%";
//							}
//							someList.add(rowToInsert);
//							auxRow = "";
//							rowToInsert= "";
//						}
//					}
//				}
//			}
//			someList.forEach(item -> {
//				System.out.println(item);
//			});
//
//		}catch (Exception e){
//			System.err.println(e);
//		}
//	}
//
//
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Test
//	void matchedPassword(){
//	 String first = "PROCESO DE PORTAFOLIO FINAL PY71461-002D";
//	 String code = "";
//	 String name = "";
//	 name = first.split("-")[0];
//	 String section = first.replace(name,"").replace("-","");
//	 code = first.split("-")[0].substring( first.split("-")[0].length()-7, first.split("-")[0].length());
//	 name = name.replace(code,"");
//	 System.out.println(name);
//	 System.out.println(code);
//	 System.out.println(section);
//
//	}
//
//
//	@Test
//	void backToStart1(){
//		File fl = new File("src/main/resources/testprofe1.pdf");
////		File fl = new File("src/main/resources/horario_2021_1.pdf");
//
//		ArrayList<String> someList = new ArrayList<>();
//		String auxRow= "";
//		String rowToInsert="";
//		String passRow = "";
//
//
//		try (PDDocument document = PDDocument.load(fl)) {
//			ObjectMapper om = new ObjectMapper();
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()) {
//				// iterate over the pages of the document
//				Page page = pi.next();
//
//				List<Table> table = sea.extract(page);
//				// iterate over the tables of the page
//				for (Table tables : table) {
//					List<List<RectangularTextContainer>> rows = tables.getRows();
//					for (List<RectangularTextContainer> cells : rows) {
//
//						// print all column-cells of the row plus linefeed
//						for (RectangularTextContainer content : cells) {
//							// Note: Cell.getText() uses \r to concat text chunks
//							System.out.println(content.getText());
//						}
////						System.out.println(row);
//
//					}
//				}
//			}
//
//
//		}catch (Exception e){
//			System.err.println(e);
//		}
//	}
//
//
//
//	@Test
//	void some(){
//
//
//
//		try {
//			// Especifica la ruta del archivo PDF
//			String pdfFilePath = "src/main/resources/testprofe1.pdf";
//
//			// Abre el documento PDF
//			PDDocument document = PDDocument.load(new File(pdfFilePath));
//			SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//			PageIterator pi = new ObjectExtractor(document).extract();
//			while (pi.hasNext()){
//				Page page = pi.next();
//				System.out.println(page.getText());
//
//			}
//
//			// Cierra el documento PDF
//			document.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//
//	@Test
//	void regexToken(){
//		String token = "$2a$10$3UN2UksSlA7Ced/tyBEtQeV0zl9nfwBWzysfFNvdGmYQV91v9Ue7e";
//		System.err.println(token.replaceAll("[!@#\\$%^&*()+=\\[\\]{};:',<>?\\\\/]", "-"));
//
//	}
//
//
	@Autowired
	OnBoardingServiceImpl ob;
	@Test
	void testMetods() throws IOException {
		File file = new File("C:/Users/jocpa/OneDrive/Documents/A test/test.pdf");
		try {
			// Lee el contenido del archivo en un array de bytes
			FileInputStream input = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			input.read(bytes);

			// Crea un MockMultipartFile a partir de los bytes del archivo
			MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "application/pdf", bytes);
			ArrayList<String> rows =  ob.readFile(multipartFile);

			List<Subject> subjectList = ob.extractSubjectListFromPdf(rows);
			System.out.println(subjectList);
			// Ahora puedes usar 'multipartFile' donde se requiera un MultipartFile
		} catch (IOException e) {
			// Manejar excepciones
		}


	}


}




