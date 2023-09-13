<h1>A simple tool to calculate bulgarian tax declaration appendix 5 and 8</h1>
<h2>Usage</h2>
<li>HELP -> mvn exec:java -Dexec.mainClass=tax.Main -Dexec.args="--help"</li>
<li>mvn exec:java -Dexec.mainClass=tax.Main -Dexec.args="--holdingsFilePath=/{TAX_PATH}/ByStatus.xlsx --soldFilePath=/{TAX_PATH}/G&L_Expanded.xlsx"</li>

<h2>BETA</h2>
There is a xml file that is generated that can be uploaded directly. <b>Use at own risk</b>
If you get a message=5 RDBMS error and the entire declaration is locked you can download https://nra.bg/wps/portal/nra/documents/documents_priority/7cce6e29-6159-4b05-a1a1-71d8316355cd and populate only part one and generate a clean xml from the after the import of the clean xml the declaration will become editable again.
