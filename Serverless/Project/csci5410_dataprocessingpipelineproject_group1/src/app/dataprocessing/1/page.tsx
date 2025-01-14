'use client'

import { useState, useEffect, useCallback, useContext } from 'react';
import { useDropzone } from 'react-dropzone';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Loader2 } from 'lucide-react';
import { FileHistory } from '@/components/file-history-dp1';
import { auth_api } from "@/lib/constants";
import { UserContext } from "@/app/contexts/user-context";
import { FeedbackDialog } from '@/components/feedback-dialog';
import { FeedbackTable } from '@/components/feedback-table';

interface FileDetails {
  fileName: string;
  referenceId: string;
  timestamp: string; // ISO date string
  txtFileLink: string;
  rowCount: string;
  status: string;
}

export default function FileUploadPage() {
  const [file, setFile] = useState<File | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [fileHistory, setFileHistory] = useState<FileDetails[]>([]);
  const [isFetching, setIsFetching] = useState(true);
  const { user } = useContext(UserContext);

  // Fetch file history
  useEffect(() => {
    const fetchFileHistory = async () => {
      try {
        const userInfo = JSON.parse(localStorage.getItem('user') || '{}');
        const email = userInfo.email;

        const getAllJobsRequest = {
          user_email: email,
        };

        const response = await fetch(
          `https://b4n62w6tscofbvbdvt7nvzxari0nvjpn.lambda-url.us-east-1.on.aws/`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(getAllJobsRequest),
          }
        );

        if (response.ok) {
          const data = await response.json();
          if (Array.isArray(data)) {
            const formattedFiles = data.map((file: any): FileDetails => ({
              fileName: file.filename ?? '-', // Ensure fileName exists
              referenceId: file.process_code ?? '-', // Fallback if process_code is missing
              timestamp: file.Timestamp
                ? new Date(file.Timestamp * 1000).toISOString()
                : '-', // Handle missing Timestamp
              txtFileLink: file.ProcessedFile ?? '-', // Ensure ProcessedFile is available
              rowCount: file.RowCount ? file.RowCount.toString() : '-', // Safely handle RowCount
              status: file.JobStatus ?? 'UNKNOWN', // Default to 'UNKNOWN' if JobStatus is missing
            }));

            // Sort by timestamp in descending order
            formattedFiles.sort(
              (a: FileDetails, b: FileDetails) =>
                new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
            );

            setFileHistory(formattedFiles);
          } else {
            console.warn("Expected an array, but got:", data);
          }
        } else {
          console.error('Error fetching file history:', await response.text());
        }
      } catch (error) {
        console.error('Error:', error);
      } finally {
        setIsFetching(false);
      }
    };

    fetchFileHistory();
  }, []);

  const onDrop = useCallback((acceptedFiles: File[]) => {
    if (acceptedFiles && acceptedFiles.length > 0) {
      setFile(acceptedFiles[0]);
    }
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setFile(event.target.files[0]);
    }
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!file) return;

    setIsLoading(true);

    try {
      const userInfo = JSON.parse(localStorage.getItem('user') || '{}');
      console.log(userInfo);
      const email = userInfo.email;
      const role = userInfo.role;
      const accessToken = userInfo.accessToken;

      const formData = new FormData();
      formData.append('file', file);
      formData.append('email', email);
      formData.append('role', role);
      formData.append('accessToken', accessToken);
      formData.append('type', "DP1");

      const response = await fetch(
        `https://nqugupik3c6c3qm46h2tehbasa0uqlnq.lambda-url.us-east-1.on.aws/`,
        {
          method: 'POST',
          body: formData,
        }
      );

      if (response.ok) {
        const updatedFile = await response.json();

        const formattedFile: FileDetails = {
          fileName: updatedFile.filename ?? '-',
          referenceId: updatedFile.process_code ?? '-',
          timestamp: new Date().toISOString(),
          txtFileLink: updatedFile.ProcessedFile ?? '-',
          rowCount: updatedFile.RowCount ? updatedFile.RowCount.toString() : '-',
          status: 'Running',
        };

        setFileHistory((prev) => [formattedFile, ...prev]);
      } else {
        console.error('Error uploading file:', await response.text());
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setIsLoading(false);
      setFile(null);
    }
  };

  if (isFetching) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Loader2 className="h-6 w-6 animate-spin" />
        <span className="ml-2">Loading...</span>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-6 py-8 min-h-screen">
      <div className="space-y-8">
        <Card className="w-full">
          <CardContent className="pt-6">
            <h2 className="text-2xl font-bold text-center mb-6">File Upload</h2>
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Drag-and-Drop Area */}
              <div
                {...getRootProps()}
                className={`p-6 border-2 border-dashed rounded-lg cursor-pointer transition-colors ${
                  isDragActive ? 'border-primary bg-primary/10' : 'border-gray-300 hover:border-primary/50'
                }`}
              >
                <input {...getInputProps()} />
                <p className="text-center">
                  {isDragActive ? (
                    <span className="text-primary">Drop the file here...</span>
                  ) : (
                    'Drag & drop a file here, or click to select one'
                  )}
                </p>
              </div>

              {/* File Selection Status */}
              <div className="text-sm text-muted-foreground text-center">
                {file ? `Selected file: ${file.name}` : 'No file selected'}
              </div>

              {/* File Upload Input */}
              <div className="relative flex items-center justify-center">
                <label className="bg-primary text-primary-foreground py-2 px-4 rounded-lg cursor-pointer hover:bg-primary/90">
                  Choose File
                  <input
                    type="file"
                    onChange={handleFileChange}
                    className="hidden"
                  />
                </label>
              </div>

              {/* Submit Button */}
              <Button type="submit" className="w-full" disabled={!file || isLoading}>
                {isLoading ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Processing
                  </>
                ) : (
                  'Submit'
                )}
              </Button>
            </form>
          </CardContent>
        </Card>

        {/* File History Section */}
        <FileHistory files={fileHistory} />

        {/* Feedback Section */}
        <FeedbackTable feature="dp1" />
        { user && user.role === "Registered" && <FeedbackDialog feature="dp1" />}
      </div>
    </div>
  );
}