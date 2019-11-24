/*
 * This class is a ValueConverter class which is responsible for changing integer values
 * to corresponding RGB objects. The input integers being the different periods of patterns
 */


package com.drew;

    public class IntToBrushConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            int LifeState = (int)value;
            switch (LifeState)            
            {
                //Alive and Unknown Periodicity
                case 0:
                    return new SolidColorBrush(Color.FromRgb(0, 0, 0));
                case 1:
                    return new SolidColorBrush(Color.FromRgb(255, 0, 0));
                case 2:
                    return new SolidColorBrush(Color.FromRgb(0, 255, 0));
                case 3:
                    return new SolidColorBrush(Color.FromRgb(0, 0, 255));
                case 4:
                    return new SolidColorBrush(Color.FromRgb(255, 255, 0));
                case 5:
                    return new SolidColorBrush(Color.FromRgb(0, 255, 255));
                case 6:
                    return new SolidColorBrush(Color.FromRgb(255, 0, 255));
                case 7:
                    return new SolidColorBrush(Color.FromRgb(192, 192, 192));
                case 8:
                    return new SolidColorBrush(Color.FromRgb(128, 128, 128));
                case 9:
                    return new SolidColorBrush(Color.FromRgb(128, 0, 0));
                case 10:
                    return new SolidColorBrush(Color.FromRgb(128, 128, 0));
                case 11:
                    return new SolidColorBrush(Color.FromRgb(0, 128, 0));
                case 12:
                    return new SolidColorBrush(Color.FromRgb(128, 0, 128));
                case 13:
                    return new SolidColorBrush(Color.FromRgb(0, 128, 128));
                case 14:
                    return new SolidColorBrush(Color.FromRgb(0, 0, 128));
                //Crimson
                case 15:
                    return new SolidColorBrush(Color.FromRgb(220, 20, 60));
                //Orange Red
                case 16:
                    return new SolidColorBrush(Color.FromRgb(255, 69, 0));
                default:
                    return null;
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            return null;
        }
    }
}
