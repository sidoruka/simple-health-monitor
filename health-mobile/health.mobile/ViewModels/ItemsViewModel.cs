using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using health.mobile.Models;
using health.mobile.Services;
using Xamarin.Forms;

namespace health.mobile
{
    public class ItemsViewModel : BaseViewModel
    {
		public MetricsDataStore DataStore = new MetricsDataStore();

        public Command LoadItemsCommand { get; set; }

        public string Error { get; set; }

        public ItemsViewModel()
        {
            Title = "Metrics";
            Items = new ObservableCollection<Agent>();
            LoadItemsCommand = new Command(async () => await ExecuteLoadItemsCommand());
        }

        async Task ExecuteLoadItemsCommand()
        {
            if (IsBusy)
                return;

            IsBusy = true;

            try
            {
                var items = await DataStore.GetItemsAsync(true);
                Items.Clear();
                foreach (var item in items)
                {
                    Items.Add(item);
                }
            }
            catch (Exception ex)
            {
                Error = ex.ToString();
            }
            finally
            {
                IsBusy = false;
            }
        }
    }
}
