package org.emoflon.ibex.neo.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.emoflon.ibex.neo.benchmark.util.BenchContainer;
import org.emoflon.ibex.neo.benchmark.util.BenchEntry;

public class ScaledBenchRunner {

	protected final Class<?> clazz;
	protected final List<String> jvmArgs;
	protected final List<List<String>> execArgs;
	protected final int repetitions;

	public ScaledBenchRunner(Class<?> clazz, List<String> jvmArgs, List<List<String>> execArgs, int repetitions) {
		this.clazz = clazz;
		this.jvmArgs = jvmArgs;
		this.execArgs = execArgs;
		this.repetitions = repetitions;
	}

	public void run() throws IOException, InterruptedException {
		BenchContainer benchCont = new BenchContainer();

		for (List<String> args : this.execArgs) {
			for (int r = 0; r < this.repetitions; r++) {
				Process process = execute(this.clazz, jvmArgs, args);
				process.waitFor();
				process.exitValue();

				InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
				BufferedReader reader = new BufferedReader(inputStreamReader);

				StringBuilder b = new StringBuilder();
				String read = reader.readLine();
				while (read != null) {
					b.append(read);
					b.append("\n");
					read = reader.readLine();
				}

				String[] output = b.toString().strip().split(";");
				BenchEntry benchEntry = new BenchEntry( //
						Integer.valueOf(output[0]), //
						Integer.valueOf(output[1]), //
						Integer.valueOf(output[2]), //
						Double.valueOf(output[3]), //
						Double.valueOf(output[4]), //
						Integer.valueOf(output[5]));
				benchCont.addBench(benchEntry);

				System.out.println(benchEntry);
			}
		}

		benchCont.print();
	}

	protected Process execute(Class<?> clazz, List<String> jvmArgs, List<String> args) throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = clazz.getName();
		List<String> command = new ArrayList<>();
		command.add(javaBin);
		command.addAll(jvmArgs);
		command.add("-cp");
		command.add(classpath);
		command.add(className);
		command.addAll(args);
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		return process;
	}

}
