using System;
using System.Collections.Generic;
using System.Threading.Tasks;

using Xamarin.Forms;
using health.mobile.Models;

namespace health.mobile
{
    public partial class ItemsPage : ContentPage
    {
        ItemsViewModel viewModel;

        public ItemsPage()
        {
            InitializeComponent();

            BindingContext = viewModel = new ItemsViewModel();
        }

        protected override void OnAppearing()
        {
            base.OnAppearing();

            if (viewModel.Items.Count == 0)
                viewModel.LoadItemsCommand.Execute(null);
        }

        void Handle_ItemTapped(object sender, Xamarin.Forms.ItemTappedEventArgs e)
        {
            if (e == null)
            {
                return;
            }
            ((ListView)sender).SelectedItem = null;
        }
    }
}
