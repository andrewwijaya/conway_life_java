package com.drew.conway.util;

public class PatternDataUtils {
    //Opens a pattern file and returns a string array which contain the pattern encoding and meta information
//    public String[] OpenPattern() {
//        OpenFileDialog dialog = new OpenFileDialog();
//        dialog.DefaultExt = ".cells";
//        Nullable<bool> result = dialog.ShowDialog();
//
//        if (result == true) {
//            Stream FileStream = dialog.OpenFile();
//            StreamReader reader = new StreamReader(FileStream);
//            string CurrentLine;
//            string Description = "";
//            string PatternEncoding = "";
//
//            while ((CurrentLine = reader.ReadLine()) != null) {
//                if (CurrentLine.StartsWith("!")) {
//                    CurrentLine = CurrentLine.TrimStart('!');
//                    Description += CurrentLine + "\n";
//                } else {
//                    PatternEncoding += CurrentLine + "\n";
//                }
//            }
//            string[] data = new string[2];
//            data[0] = Description;
//            data[1] = PatternEncoding;
//            return data;
//        } else {
//            return null;
//        }
//    }
//
//    //Takes a method encoding and saves it into a .cells file
//    public void SavePatternToFile(string PatternEncoding) {
//        Microsoft.Win32.SaveFileDialog dlg = new Microsoft.Win32.SaveFileDialog();
//        dlg.FileName = "CellFile"; // Default file name
//        dlg.DefaultExt = ".cells"; // Default file extension
//
//        // Show save file dialog box
//        Nullable<bool> result = dlg.ShowDialog();
//
//        // Process save file dialog box results
//        if (result == true) {
//            // Save document
//            string filename = dlg.FileName;
//            File.WriteAllText(filename, PatternEncoding);
//        }
//    }
}