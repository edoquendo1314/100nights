package com.example.nights;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import javax.imageio.spi.IIORegistry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.title;

public class GoogleAuthorizeUtil {
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String APPLICATION_NAME = "100nights";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

//    public static Credential authorize() throws IOException, GeneralSecurityException {
//
//        // build GoogleClientSecrets from JSON file
//
//        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
//
//        // build Credential object
//
//        return credential;
//    }
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        //final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "A1";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        //CREATE THE SHEET
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle("100 Nights"));
        spreadsheet = service.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();
        System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());

        String[] studentString = studentList();
        //WRITE
        List<List<Object>> values = Arrays.asList(
                Arrays.asList(
                       studentString
                )
                // Additional rows ...
        );
        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange()
                .setRange(range)
                .setValues(values));
//           Additional ranges to update ...

        BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                .setValueInputOption("RAW")
                .setData(data);
        BatchUpdateValuesResponse result =
                service.spreadsheets().values().batchUpdate(spreadsheet.getSpreadsheetId(), body).execute();
        System.out.printf("%d cells updated.", result.getTotalUpdatedCells());
//
    }

    public static String[] studentList()
    {
       String[] stringArray = new String[]
            {
                    "Kelly Cameron",
                    "Behar Ariella",
                    "Weiss Samuel",
                    "Colvin Sean",
                    "Dye Charles",
                    "Simpson Carl",
                    "Stevens Zemira",
                    "Epstein Serena",
                    "Blackman Dylan",
                    "Paquette Alexandra",
                    "Yan Iris",
                    "Horowitz Melissa",
                    "Lucero Leo",
                    "Greene Austin",
                    "Siegel Arianna",
                    "Nasim Nico",
                    "Craven Eve",
                    "Svenningsen Morgan",
                    "Chiang Jeffrey",
                    "DiPaola Oona",
                    "Chan Eric",
                    "Moltner Joelle",
                    "Berger Kaden",
                    "Sozio Mitchell",
                    "Satsky Preston",
                    "Graham Natalie",
                    "Silpe Luke",
                    "Newman Gabrielle",
                    "Reiner Samuel",
                    "Chen Leo",
                    "Tricot Tyler",
                    "Lulla Anya",
                    "Rubin Noah",
                    "Schulman Jack",
                    "Wurzel Benjamin",
                    "Askari Aiden",
                    "Murphy Zachary",
                    "Blackstock Beauregard",
                    "Becket Vanessa",
                    "Gomprecht Rebecca",
                    "Kingsley James",
                    "Jacobs Jasper",
                    "Hendler Julian",
                    "Carrasco Ryan",
                    "Cohen Robert",
                    "Grossman Alexandra",
                    "Seaman Frank",
                    "Chen Angela",
                    "Rosenstock Samuel",
                    "Korin Darius",
                    "Yevdokimov Nikita",
                    "Kordes Alexander",
                    "Sack Rebecca",
                    "Beaird Molly",
                    "Chereskin Anderson",
                    "Siegal Jay",
                    "Riley Shireman Charles",
                    "Cameron Arielle",
                    "Leibman Zachary",
                    "Roth Hank"
            };
       return stringArray;
    }

}
//List<List<Object>> values = Arrays.asList(
//                Arrays.asList(
//                        "hello"
//                )
//                // Additional rows ...
//        );
//        ValueRange body = new ValueRange()
//                .setValues(values);
//        UpdateValuesResponse result =
//                service.spreadsheets().values().update(spreadsheet.getSpreadsheetId(), range, body)
//                        .setValueInputOption("RAW")
//                        .execute();
//        System.out.printf("%d cells updated.", result.getUpdatedCells());