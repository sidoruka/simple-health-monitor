using System;

using Xamarin.Forms;

namespace health.mobile
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();
            MainPage = new ItemsPage();
        }
    }
}
