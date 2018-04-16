using System;
using System.Globalization;
using Xamarin.Forms;

namespace health.mobile
{
    public class StateFontWeightConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value.ToString() == "FAIL")
            {
                return FontAttributes.Bold;
            }
                return FontAttributes.None;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
