# eMoflon IBeX x Neo Benchmark Zoo

## How to Run

1. Follow the instructions of eMoflon::IBeX developement setup until point 11 at:<br>
   https://github.com/eMoflon/emoflon-ibex/blob/master/README.md
3. Set up your runtime workspace by starting a runtime Eclipse Application from your development workspace:
    - To start the runtime workspace, do the following steps inside your development workspace: Run -> Run Configurations...; double click on Eclipse Application, give it a name (e.g. benchmark-workspace) and click on Run.
    - Inside your runtime workspace, import this PSF file:<br>
      https://raw.githubusercontent.com/eMoflon/emoflon-ibex-neo-benchmark/master/benchProjectSet.psf
4. Inside the runtime workspace, build all projects (*Project &rarr; Clean... &rarr; Clean all projects*) to trigger code generation.
	- Hint: It may be required to trigger a full eMoflon build on all projects. Select all projects in *Package Explorer* and click on the black hammer symbol.
5. Inside the ```org.emoflon.ibex-neo-benchmark``` project you can find various benchmark sets. To execute a benchmark, search for a package ending with ```.run``` and run the contained classes as Java Application.
6. To view the meassurements of a benchmark run, copy & paste the CSV-like console output into a spreadsheet.
