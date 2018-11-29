import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PerconlationVisualizerInvoker {
	public static void main(String[] args) throws IOException {
		Files.list(Paths.get("./src/"))
			.map(p -> p.getFileName()
				.toFile()
				.getName())
			.filter(p -> p.endsWith("txt"))
			.forEach(PercolationVisualizer::new);

	}
}
