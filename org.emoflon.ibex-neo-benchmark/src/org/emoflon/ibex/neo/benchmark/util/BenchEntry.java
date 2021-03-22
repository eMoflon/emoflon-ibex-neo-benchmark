package org.emoflon.ibex.neo.benchmark.util;

public class BenchEntry {

	public int n;
	public int c;
	public int elts;
	public double init;
	public double resolve;
	public int ram;

	public BenchEntry(int n, int c, int elts, double init, double resolve, int ram) {
		this.n = n;
		this.c = c;
		this.elts = elts;
		this.init = init;
		this.resolve = resolve;
		this.ram = ram;
	}

	@Override
	public String toString() {
		return n + ";" + c + ";" + elts + ";" + init + ";" + resolve + ";" + ram;
	}

}
