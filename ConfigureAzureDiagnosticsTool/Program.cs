using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.WindowsAzure.Diagnostics;
using System.IO;
using System.Xml.Serialization;
using Microsoft.WindowsAzure;
using System.Diagnostics;

namespace ConfigureAzureDiagnostics
{
    class Program
    {
        static void Main(string[] args)
        {
            string configXmlDir = Environment.CurrentDirectory;
            if (args.Length == 1)
            {
                configXmlDir = args[0];
            }
            string configXmlPath = Path.Combine(configXmlDir, "DiagnosticsConfiguration.xml");
            if (!File.Exists(configXmlPath))
            {
                throw new InvalidOperationException(string.Format("Unable to find diagnostics configuration xml @ {0}", configXmlPath));
            }

            XmlSerializer serializer = new XmlSerializer(typeof(DiagnosticsConfig));
            DiagnosticsConfig diagConfig = null;
            using (Stream fs = File.OpenRead(configXmlPath))
            {
                diagConfig = serializer.Deserialize(fs) as DiagnosticsConfig;
            }

            DiagnosticMonitorConfiguration config = DiagnosticMonitor.GetDefaultInitialConfiguration();
            config.OverallQuotaInMB = diagConfig.OverallQuotaInMB;

            if (diagConfig.Directories.Directory != null)
            {
                foreach (DiagnosticsConfigDirectoriesDirectory dir in diagConfig.Directories.Directory)
                {
                    DirectoryConfiguration directoryConfig = new DirectoryConfiguration();
                    directoryConfig.Container = dir.ContainerName;
                    if (dir.IsLocalPathRelative)
                    {
                        directoryConfig.Path = Path.Combine(Environment.CurrentDirectory, dir.LocalPath);
                    }
                    else
                    {
                        directoryConfig.Path = dir.LocalPath;
                    }
                    directoryConfig.DirectoryQuotaInMB = dir.DirectoryQuotaInMB;
                    config.Directories.DataSources.Add(directoryConfig);
                }
                config.Directories.ScheduledTransferPeriod = TimeSpan.FromSeconds(diagConfig.Directories.ScheduledTransferPeriodInSeconds);
            }

            if (diagConfig.PerformanceCounters.PerformanceCounter != null)
            {
                foreach (DiagnosticsConfigPerformanceCountersPerformanceCounter perf in diagConfig.PerformanceCounters.PerformanceCounter)
                {
                    PerformanceCounterConfiguration perfConfig = new PerformanceCounterConfiguration();
                    perfConfig.CounterSpecifier = perf.CounterName;
                    perfConfig.SampleRate = TimeSpan.FromSeconds(perf.SamplingRateInSeconds);
                    config.PerformanceCounters.DataSources.Add(perfConfig);
                }
                config.PerformanceCounters.ScheduledTransferPeriod = TimeSpan.FromSeconds(diagConfig.PerformanceCounters.ScheduledTransferPeriodInSeconds);
            }

            DiagnosticMonitor.Start(CloudStorageAccount.Parse(diagConfig.StorageAccountConnectionString), config);

            Trace.WriteLine("ConfigureAzureDiagnostics Started.", "Information");

            Console.ReadLine();
        }
    }
}
