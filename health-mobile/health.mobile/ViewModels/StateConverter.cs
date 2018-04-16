using System;
using System.Globalization;
using Xamarin.Forms;

namespace health.mobile
{
    public class StateConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value.ToString() == "OK") {
                return Color.Green;
            } else if (value.ToString() == "FAIL") {
                return Color.Red;
            }
            return Color.DarkGray;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
