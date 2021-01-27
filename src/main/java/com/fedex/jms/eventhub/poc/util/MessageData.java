package com.fedex.jms.eventhub.poc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fedex.jms.eventhub.poc.model.MaterialComposition;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class MessageData {

	private static final Logger log = LoggerFactory.getLogger(MessageData.class);

	public MessageData() {

	}

	public static final List<Map<?, ?>> covertCSVDataToList() {

		try {
			URL path = MessageData.class.getClassLoader().getResource("SampleCSVFileWithQuotes.csv");
			File input = new File(path.toURI());
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			log.info("******************* List in messageData *********** :: " + list);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static final List<Map<?, ?>> withQuotesCovertCSVDataToList() {

		try {
			URL path = MessageData.class.getClassLoader().getResource("SampleCSVFile.csv");
			File input = new File(path.toURI());

			File output = new File("/Users/name/output.json");

			CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
			CsvMapper csvMapper = new CsvMapper();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);

			// Read data from CSV file
			List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();
			log.info("******************* List in readAll *********** :: " + readAll);

			// mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);

			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csvSchema)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			log.info("******************* List in messageData *********** :: " + list);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final void readCSVTestData() {

		URL path = MessageData.class.getClassLoader().getResource("com/fedex/jms/poc/SampleCSVFile.csv");

		CSVReader reader;
		try {
			File f = new File(path.toURI());
			reader = new CSVReader(new FileReader(f), ',');

			HeaderColumnNameMappingStrategy<MaterialComposition> beanStrategy = new HeaderColumnNameMappingStrategy<MaterialComposition>();
			beanStrategy.setType(MaterialComposition.class);

			CsvToBean<MaterialComposition> csvToBean = new CsvToBean<MaterialComposition>();
			List<MaterialComposition> materials = csvToBean.parse(beanStrategy, reader);
			log.info(materials.toString());
		} catch (FileNotFoundException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
