package com.everis.ocrworker;

import com.abbyy.FREngine.*;


/**
 * Clase que integrará el motor de reconocimiento de textos de ABBYY Finereader para poder ser usado en
 * este mismo proyecto.
 */
public class FinereaderImpl {

    public FinereaderImpl() {}

    public String Run(String imagePath, String exportPath, Integer maxVolume, Integer actualVolume, Integer precision) throws Exception {
        String result = "";
        displayMessage( "Initializing Engine..." );
        engine = Engine.GetEngineObject("C:\\Program Files (x86)\\ABBYY SDK\\11\\FlexiCapture Engine\\Bin", "SWET11030006143815230224");

        try {
            // Setup FREngine
            displayMessage( "Loading predefined profile..." );
            //engine.LoadPredefinedProfile( "TextExtraction_Accuracy" );

            // Process sample image
            try{
                // Create document

                if(precision.equals(1))
                    engine.LoadPredefinedProfile( "TextExtraction_Speed" );

                else
                    engine.LoadPredefinedProfile( "TextExtraction_Accuracy" );

                displayMessage( "Loading image..." );
                IFRDocument document = engine.CreateFRDocument();



                if (precision.equals(1) || precision.equals(2)){
                    document.AddImageFile(imagePath,null,null);
                }

                else {
                    IPrepareImageMode pim = engine.CreatePrepareImageMode();

                    pim.setAutoOverwriteResolution(true);
                    pim.setEnhanceLocalContrast(true);
                    document.AddImageFile(imagePath, pim, null);
                }




                Integer numPag = document.getPages().getCount();
                if(numPag + actualVolume > maxVolume) return "Max volume exceeded!";
                else {
                    try {
                        // Add image file to document
                        displayMessage( "Prepocessing image..." );



                        if(precision.equals(3)) {
                            IPageProcessingParams ppp = engine.CreatePageProcessingParams();
                            IPagePreprocessingParams pppp = ppp.getPagePreprocessingParams();
                            pppp.setCorrectOrientation(true);
                            pppp.setCorrectShadowsAndHighlights(ThreeStatePropertyValueEnum.TSPV_Yes);
                            for(int i = 0; i < numPag; i++ ) {
                                IFRPage page = document.getPages().Item(i);

                                if(!page.getImageDocument().getImageColorType().equals(ImageColorTypeEnum.ICT_BlackWhite)) {
                                    page.getImageDocument().EnhanceLocalContrast();
                                    page.getImageDocument().CorrectShadowsAndHighlights();
                                    page.getImageDocument().CropImage();
                                }
                                else {
                                    page.getImageDocument().CropImage();
                                }
                                IObjectsExtractionParams obj = engine.CreateObjectsExtractionParams();
                                obj.setRemoveGarbage(true);
                                page.Preprocess(pppp,obj,null);

                            }

                        }

                        else if(precision.equals(2)) {
                            IPageProcessingParams ppp = engine.CreatePageProcessingParams();
                            IPagePreprocessingParams pppp = ppp.getPagePreprocessingParams();
                            pppp.setCorrectOrientation(true);
                            for(int i = 0; i < numPag; i++ ) {
                                IFRPage page = document.getPages().Item(i);
                                page.Preprocess(pppp,null,null);
                            }
                        }


                        // Process document
                        displayMessage( "Process..." );

                        IDocumentProcessingParams dpp = engine.CreateDocumentProcessingParams();

                        if (precision.equals(3)) {
                            IPageAnalysisParams analysis = engine.CreatePageAnalysisParams();
                            analysis.setDetectTables(true);
                            analysis.setDetectText(true);

                            dpp.getPageProcessingParams().getRecognizerParams().SetPredefinedTextLanguage("Spanish,English,PortugueseStandard");
                            dpp.getPageProcessingParams().setPageAnalysisParams(analysis);
                            dpp.getPageProcessingParams().setPerformRecognition(true);
                        }

                        else {
                            dpp.getPageProcessingParams().getRecognizerParams().SetPredefinedTextLanguage("Spanish,English,PortugueseStandard");
                        }

                        document.Process( dpp );


                        //Safe results to TXT
                        ITextExportParams textParams = engine.CreateTextExportParams();
                        document.Export(exportPath, FileExportFormatEnum.FEF_TextUnicodeDefaults, textParams);

                        result = numPag.toString();

                    } finally {
                        // Close document
                        document.Close();
                    }
                }

            }catch( Exception ex ) {
                result =  "Error " + ex.getMessage();
            }
        }  catch( Exception ex ) {
            result = "Error " + ex.getMessage();
        }


        displayMessage( "Deinitializing Engine..." );
        engine = null;
        Engine.DeinitializeEngine();
        return result;

    }


    private static void displayMessage( String message ) {
        System.out.println( message );
    }

    private IEngine engine = null;
}