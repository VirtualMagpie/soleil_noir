package fr.virtualmagpie.soleilnoir.service;

import com.opencsv.CSVWriter;
import fr.virtualmagpie.soleilnoir.config.StatConfig;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.stat.CardDrawStatistics;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** This service save statistic result as csv files */
@Slf4j
public class ExportService {

  public void exportStatistics(StatConfig statConfig, CardDrawStatistics statResult) {
    try {
      Path path = Path.of("export", computeFileName(statConfig));
      Files.deleteIfExists(path);
      // Files.createFile(path);
      writeFile(path, statResult);
    } catch (IOException e) {
      log.error("Could not export result", e);
    }
  }

  private String computeFileName(StatConfig statConfig) {
    return String.format(
        "soleil-noir-statistics_%s_%s.csv",
        statConfig.getCombinationStrategyName(), statConfig.getNbTry());
  }

  private void writeFile(Path path, CardDrawStatistics statResult) throws IOException {
    try (CSVWriter writer =
        new CSVWriter(
            new FileWriter(path.toString()),
            ';',
            CSVWriter.DEFAULT_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END)) {

      // Write header
      List<String> header =
          statResult.getDifficulties().stream()
              .map(Combination::toString)
              .collect(Collectors.toList());
      header.add(0, "Nb cards / Combination difficulty");
      writer.writeNext(header.toArray(String[]::new));

      // Write statistics line for each number of card
      for (int nbCards : statResult.getCardNumbers()) {
        List<String> percentages = new ArrayList<>();
        percentages.add(String.valueOf(nbCards));
        for (Combination difficulty : statResult.getDifficulties()) {
          percentages.add(String.format("%.2f %%", 100 * statResult.getStat(difficulty, nbCards)));
        }
        writer.writeNext(percentages.toArray(String[]::new));
      }
    }
  }
}
