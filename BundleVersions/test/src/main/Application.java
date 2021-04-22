/**
 * Main class of report-validator application
 */
public class Application extends GroovyObjectSupport implements ParameterConversion {
    /**
     * Runs report validation
     *
     * @param args application arguments
     * @return exit code
     */
    public int run(java.lang.String[] args) {
        try {
            OptionAccessor options = parseOptions(args);
            if (!options.asBoolean()) {
                return 2;
            }

            java.lang.Object optionMap = invokeMethod("optionsToMap", new java.lang.Object[]{options});
            httpClient = optionMap.verbose ? new OkHttpClient.Builder().invokeMethod("addInterceptor", new java.lang.Object[]{new HttpLoggingInterceptor().invokeMethod("setLevel", new java.lang.Object[]{HttpLoggingInterceptor.Level.BODY})}).invokeMethod("build", new java.lang.Object[0]) : new OkHttpClient();

            return runImplementation((java.util.Map) optionMap);

        } catch (RecognizedError recognizedError) {
            invokeMethod("println", new java.lang.Object[]{"error:" + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{recognizedError.message})});
            return 2;
        } catch (java.lang.Exception exception) {
            invokeMethod("println", new java.lang.Object[]{"error:"});
            StackTraceUtils.invokeMethod("sanitize", new java.lang.Object[]{exception});
            exception.printStackTrace(java.lang.System.out);
            return 3;
        }

    }

    public OptionAccessor parseOptions(java.lang.String[] args) {
        CliBuilder cli = new CliBuilder();

        cli.invokeMethod("with", new java.lang.Object[]{new Closure(this, this) {
            public java.lang.Object doCall(java.lang.Object it) {
                usage = "java -jar report-validator.jar [options]";
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map.put("longOpt", "mode");
                map.put("args", 1);
                map.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map, "run mode. allowed values are save, compare"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map1 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map1.put("longOpt", "pentahopath");
                map1.put("args", 1);
                map1.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map1, "absolute path of report folder in BA server"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map2 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map2.put("longOpt", "baserverip");
                map2.put("args", 1);
                map2.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map2, "BA server IP"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map3 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map3.put("longOpt", "baserverport");
                map3.put("args", 1);
                map3.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map3, "BA server port"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map4 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map4.put("longOpt", "bauser");
                map4.put("args", 1);
                map4.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map4, "BA server username"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map5 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map5.put("longOpt", "bapassword");
                map5.put("args", 1);
                map5.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map5, "BA server password"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map6 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map6.put("longOpt", "localpath");
                map6.put("args", 1);
                map6.put("required", true);
                invokeMethod("_", new java.lang.Object[]{map6, "localpath "});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map7 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(2);
                map7.put("longOpt", "verbose");
                map7.put("required", false);
                invokeMethod("_", new java.lang.Object[]{map7, "verbose mode"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map8 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(3);
                map8.put("longOpt", "difftool");
                map8.put("args", 1);
                map8.put("required", false);
                invokeMethod("_", new java.lang.Object[]{map8, "(compare mode) diff tool to view compare output (defaults to diff)"});
                java.util.LinkedHashMap<java.lang.String, java.io.Serializable> map9 = new java.util.LinkedHashMap<java.lang.String, java.io.Serializable>(2);
                map9.put("longOpt", "preserveresults");
                map9.put("required", false);
                return invokeMethod("_", new java.lang.Object[]{map9, "(compare mode) preserves results (results are deleted by default)"});
            }

            public java.lang.Object doCall() {
                return doCall(null);
            }

        }});

        return ((OptionAccessor) (cli.invokeMethod("parse", new java.lang.Object[]{args})));
    }

    public int runImplementation(java.util.Map options) {
        java.lang.Object mode = invokeMethod("asString", new java.lang.Object[]{options, "mode"});
        java.lang.Object pentahoPath = invokeMethod("asString", new java.lang.Object[]{options, "pentahopath"});
        java.lang.Object localPath = invokeMethod("asString", new java.lang.Object[]{options, "localpath"});
        java.lang.Object encodedPentahoPath = PentahoRepoUtils.invokeMethod("encodePath", new java.lang.Object[]{pentahoPath});
        verbose = options.verbose;
        java.lang.Object baServerIP = invokeMethod("asString", new java.lang.Object[]{options, "baserverip"});
        java.lang.Object baServerPort = invokeMethod("asInteger", new java.lang.Object[]{options, "baserverport"});
        final java.lang.Object baUser = invokeMethod("asString", new java.lang.Object[]{options, "bauser"});
        final java.lang.Object baPassword = invokeMethod("asString", new java.lang.Object[]{options, "bapassword"});

        java.util.LinkedHashMap<java.lang.String, GString> map = new java.util.LinkedHashMap<java.lang.String, GString>(3);
        map.put("ip", baServerIP);
        map.put("port", baServerPort);
        map.put("authHeader", "Basic " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{((GString) java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{baUser}) + ":" + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{baPassword})).getBytes().invokeMethod("encodeBase64", new java.lang.Object[0])}));
        java.util.LinkedHashMap<java.lang.String, GString> baConfig = map;

        switch (mode) {
            case save:
                saveReports(baConfig, (java.lang.String) encodedPentahoPath, (java.lang.String) localPath);
                return 0;
            case compare:
                final java.lang.Object difftool = options.difftool;
                java.lang.Object diffTool = difftool.asBoolean() ? difftool : "diff";
                return compareReports(baConfig, encodedPentahoPath, localPath, diffTool, options.preserveresults);
            default:
                RecognizedError.invokeMethod("throwUnless", new java.lang.Object[]{false, "unknown mode"});
                break;
        }
    }

    public int compareReports(java.util.Map baConfig, java.lang.String encodedPentahoPath, java.lang.String expectedFolderLocation, java.lang.String diffTool, java.lang.Boolean preserveResults) {
        java.lang.String tmpAppFolder = "/tmp/report-validator";
        java.lang.Object folderName = encodedPentahoPath.split(":").invokeMethod("last", new java.lang.Object[0]);
        GString tempFolder = tmpAppFolder + "/" + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{folderName});

        saveReports(baConfig, encodedPentahoPath, tempFolder);

        int exitCode = executeDiffCommand("diff", expectedFolderLocation, tempFolder, diffTool.equals("diff"));
        if (exitCode == 0) {
            printOnVerbose("compare successful");
            Utils.invokeMethod("deleteFolder", new java.lang.Object[]{tempFolder});
            return 0;
        }

        if (!diffTool.equals("diff")) {
            executeDiffCommand(diffTool, expectedFolderLocation, tempFolder, true);
        }

        if (!preserveResults.asBoolean()) {
            Utils.invokeMethod("deleteFolder", new java.lang.Object[]{tempFolder});
        }

        printOnVerbose("compare failed");
        return 1;
    }

    public int executeDiffCommand(java.lang.String diffTool, java.lang.String expectedFolderLocation, java.lang.String actualFolderLocation, java.lang.Boolean printOutput) {
        final java.util.List<java.lang.String> diffCommand = new java.util.ArrayList<java.lang.String>(java.util.Arrays.asList(diffTool, expectedFolderLocation, actualFolderLocation));
        printOnVerbose("executing command " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{diffCommand.invokeMethod("join", new java.lang.Object[]{" "})}));
        java.lang.Object diffProcess = diffCommand.invokeMethod("execute", new java.lang.Object[0]);
        printOutput ? diffProcess.invokeMethod("consumeProcessOutput", new java.lang.Object[]{java.lang.System.out, java.lang.System.err}) : diffProcess.invokeMethod("consumeProcessOutput", new java.lang.Object[0]);
        return ((int) (diffProcess.invokeMethod("waitFor", new java.lang.Object[0])));
    }

    public void saveReports(final java.util.Map baConfig, java.lang.String encodedPentahoPath, final java.lang.String outputPath) {
        Utils.invokeMethod("deleteFolder", new java.lang.Object[]{outputPath});
        java.util.Map reportPaths = getReportLocations(baConfig, encodedPentahoPath);
        final java.lang.String outputFileExtension = ".json";
        (reportPaths == null ? null : reportPaths.each).call(new Closure(this, this) {
            public void doCall(java.lang.Object name, java.lang.Object path) {
                java.lang.Object encodedReportPath = PentahoRepoUtils.invokeMethod("encodePath", new java.lang.Object[]{path});
                saveReport(baConfig, (java.lang.String) encodedReportPath, outputPath, java.lang.String.valueOf(name) + outputFileExtension);
            }

        });
        printOnVerbose("save successful");
    }

    public void saveReport(java.util.Map baConfig, java.lang.String reportLocation, java.lang.String outputLocation, java.lang.String fileName) {
        java.lang.String reportCSV = getReportCSV(baConfig, reportLocation);
        java.lang.Object jsonReport = Utils.invokeMethod("csvToJson", new java.lang.Object[]{reportCSV});
        printOnVerbose("saving " + reportLocation + " at " + outputLocation + "/" + fileName);
        Utils.invokeMethod("saveFile", new java.lang.Object[]{outputLocation, jsonReport, fileName});
    }

    public java.util.Map getReportLocations(java.util.Map baConfig, java.lang.String encodedPentahoPath) {
        final GString path = "analytics/api/repo/files/" + encodedPentahoPath + "/children";
        java.lang.Object url = new HttpUrl.Builder().invokeMethod("scheme", new java.lang.Object[]{"http"}).invokeMethod("host", new java.lang.Object[]{baConfig.ip}).invokeMethod("port", new java.lang.Object[]{baConfig.port}).invokeMethod("addPathSegments", new java.lang.Object[]{path}).invokeMethod("addQueryParameter", new java.lang.Object[]{"filter", "FILES|*.xanalyzer"}).invokeMethod("build", new java.lang.Object[0]);
        final java.lang.Object request = new Request.Builder().invokeMethod("url", new java.lang.Object[]{url}).invokeMethod("header", new java.lang.Object[]{"Authorization", baConfig.authHeader}).invokeMethod("header", new java.lang.Object[]{"Accept", "application/json"}).invokeMethod("build", new java.lang.Object[0]);
        return ((java.util.Map) (RecognizedError.invokeMethod("guard", new java.lang.Object[]{java.io.IOException, "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed", new Closure(this, this) {
            public java.lang.Object doCall(java.lang.Object it) {
                return getHttpClient().invokeMethod("newCall", new java.lang.Object[]{request}).invokeMethod("execute", new java.lang.Object[0]).invokeMethod("withCloseable", new java.lang.Object[]{new Closure(DUMMY__1234567890_DUMMYYYYYY___.this, DUMMY__1234567890_DUMMYYYYYY___.this) {
                    public java.lang.Object doCall(java.lang.Object response) {
                        RecognizedError.invokeMethod("throwUnless", new java.lang.Object[]{!response.invokeMethod("code", new java.lang.Object[0]).equals(401), "unauthorized access"});
                        RecognizedError.invokeMethod("throwUnless", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0]).equals(200), "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed with response " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0])})});
                        java.lang.Object json = new JsonSlurper().invokeMethod("parseText", new java.lang.Object[]{response.invokeMethod("body", new java.lang.Object[0]).invokeMethod("string", new java.lang.Object[0])});
                        return (json == null ? null : json.repositoryFileDto).invokeMethod("collectEntries", new java.lang.Object[]{new Closure(DUMMY__1234567890_DUMMYYYYYY___.this, DUMMY__1234567890_DUMMYYYYYY___.this) {
                            public java.util.LinkedHashMap doCall(java.lang.Object it) {
                                java.util.LinkedHashMap map = new java.util.LinkedHashMap(1);
                                map.put(it.name, it.path);
                                return map;
                            }

                            public java.util.LinkedHashMap doCall() {
                                return doCall(null);
                            }

                        }});
                    }

                }});
            }

            public java.lang.Object doCall() {
                return doCall(null);
            }

        }})));
    }

    public java.lang.String getReportDefinition(java.util.Map baConfig, java.lang.String encodedPentahoReportPath) {
        final GString path = "/analytics/api/repos/" + encodedPentahoReportPath + "/content";
        java.lang.Object url = new HttpUrl.Builder().invokeMethod("scheme", new java.lang.Object[]{"http"}).invokeMethod("host", new java.lang.Object[]{baConfig.ip}).invokeMethod("port", new java.lang.Object[]{baConfig.port}).invokeMethod("addPathSegments", new java.lang.Object[]{path}).invokeMethod("build", new java.lang.Object[0]);
        final java.lang.Object request = new Request.Builder().invokeMethod("url", new java.lang.Object[]{url}).invokeMethod("header", new java.lang.Object[]{"Authorization", baConfig.authHeader}).invokeMethod("build", new java.lang.Object[0]);
        return ((java.lang.String) (RecognizedError.invokeMethod("guard", new java.lang.Object[]{java.io.IOException, "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed", new Closure(this, this) {
            public java.lang.Object doCall(java.lang.Object it) {
                return getHttpClient().invokeMethod("newCall", new java.lang.Object[]{request}).invokeMethod("execute", new java.lang.Object[0]).invokeMethod("withCloseable", new java.lang.Object[]{new Closure(DUMMY__1234567890_DUMMYYYYYY___.this, DUMMY__1234567890_DUMMYYYYYY___.this) {
                    public java.lang.Object doCall(java.lang.Object response) {
                        RecognizedError.invokeMethod("throwUnless", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0]).equals(200), "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed with response " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0])})});
                        return response.invokeMethod("body", new java.lang.Object[0]).invokeMethod("string", new java.lang.Object[0]);
                    }

                }});
            }

            public java.lang.Object doCall() {
                return doCall(null);
            }

        }})));
    }

    public java.lang.String getRequestId(java.util.Map baConfig, java.lang.String encodedPentahoReportPath) {
        final GString path = "analytics/api/repos/" + encodedPentahoReportPath + "/service/ajax/initRequest";
        java.lang.String reportDefinition = getReportDefinition(baConfig, encodedPentahoReportPath);
        java.lang.Object url = new HttpUrl.Builder().invokeMethod("scheme", new java.lang.Object[]{"http"}).invokeMethod("host", new java.lang.Object[]{baConfig.ip}).invokeMethod("port", new java.lang.Object[]{baConfig.port}).invokeMethod("addPathSegments", new java.lang.Object[]{path}).invokeMethod("build", new java.lang.Object[0]);
        java.lang.Object formBody = new FormBody.Builder().invokeMethod("add", new java.lang.Object[]{"reportXML", reportDefinition}).invokeMethod("add", new java.lang.Object[]{"action", "REFRESH"}).invokeMethod("build", new java.lang.Object[0]);
        final java.lang.Object request = new Request.Builder().invokeMethod("url", new java.lang.Object[]{url}).invokeMethod("header", new java.lang.Object[]{"Authorization", baConfig.authHeader}).invokeMethod("post", new java.lang.Object[]{formBody}).invokeMethod("build", new java.lang.Object[0]);
        return ((java.lang.String) (RecognizedError.invokeMethod("guard", new java.lang.Object[]{java.io.IOException, "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed", new Closure(this, this) {
            public java.lang.Object doCall(java.lang.Object it) {
                return getHttpClient().invokeMethod("newCall", new java.lang.Object[]{request}).invokeMethod("execute", new java.lang.Object[0]).invokeMethod("withCloseable", new java.lang.Object[]{new Closure(DUMMY__1234567890_DUMMYYYYYY___.this, DUMMY__1234567890_DUMMYYYYYY___.this) {
                    public java.lang.Object doCall(java.lang.Object response) {
                        RecognizedError.invokeMethod("throwUnless", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0]).equals(200), "POST " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed with response " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0])})});
                        return response.invokeMethod("body", new java.lang.Object[0]).invokeMethod("string", new java.lang.Object[0]);
                    }

                }});
            }

            public java.lang.Object doCall() {
                return doCall(null);
            }

        }})));
    }

    public java.lang.String getReportCSV(java.util.Map baConfig, java.lang.String encodedPentahoReportPath) {
        final GString path = "analytics/api/repos/" + encodedPentahoReportPath + "/service/ajax/getReportInFormat";
        java.lang.String requestId = getRequestId(baConfig, encodedPentahoReportPath);
        java.lang.Object url = new HttpUrl.Builder().invokeMethod("scheme", new java.lang.Object[]{"http"}).invokeMethod("host", new java.lang.Object[]{baConfig.ip}).invokeMethod("port", new java.lang.Object[]{baConfig.port}).invokeMethod("addPathSegments", new java.lang.Object[]{path}).invokeMethod("addQueryParameter", new java.lang.Object[]{"requestId", requestId}).invokeMethod("addQueryParameter", new java.lang.Object[]{"format", "CSV"}).invokeMethod("build", new java.lang.Object[0]);
        final java.lang.Object request = new Request.Builder().invokeMethod("url", new java.lang.Object[]{url}).invokeMethod("header", new java.lang.Object[]{"Authorization", baConfig.authHeader}).invokeMethod("build", new java.lang.Object[0]);
        return ((java.lang.String) (RecognizedError.invokeMethod("guard", new java.lang.Object[]{java.io.IOException, "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed", new Closure(this, this) {
            public java.lang.Object doCall(java.lang.Object it) {
                return getHttpClient().invokeMethod("newCall", new java.lang.Object[]{request}).invokeMethod("execute", new java.lang.Object[0]).invokeMethod("withCloseable", new java.lang.Object[]{new Closure(DUMMY__1234567890_DUMMYYYYYY___.this, DUMMY__1234567890_DUMMYYYYYY___.this) {
                    public java.lang.Object doCall(java.lang.Object response) {
                        RecognizedError.invokeMethod("throwUnless", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0]).equals(200), "GET " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{path}) + " failed with response " + java.lang.String.class.invokeMethod("valueOf", new java.lang.Object[]{response.invokeMethod("code", new java.lang.Object[0])})});
                        return response.invokeMethod("body", new java.lang.Object[0]).invokeMethod("string", new java.lang.Object[0]);
                    }

                }});
            }

            public java.lang.Object doCall() {
                return doCall(null);
            }

        }})));
    }

    public void printOnVerbose(java.lang.String text) {
        if (verbose) {
            invokeMethod("println", new java.lang.Object[]{text});
        }

    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public java.lang.Boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(java.lang.Boolean verbose) {
        this.verbose = verbose;
    }

    private OkHttpClient httpClient;
    private java.lang.Boolean verbose;
}
