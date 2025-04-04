'use client'

import { useState, useEffect, useCallback, useContext } from 'react';
import { useDropzone } from 'react-dropzone';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Loader2 } from 'lucide-react';
import { FileHistory } from '@/components/file-history-dp2';
import { auth_api } from "@/lib/constants";
import { UserContext } from '@/app/contexts/user-context';
import { FeedbackDialog } from '@/components/feedback-dialog';
import { FeedbackTable } from '@/components/feedback-table';

interface FileDetails {
  referenceId: string;
  txtFileLink: string;
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

        const getAllJobsRequest = { user_email: email };

        const response = await fetch(
          `https://q3mhoyo8i9.execute-api.us-east-1.amazonaws.com/prod/nerprocess/retrieve-files`,
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

          // Ensure file_locations exists and is an array
          if (Array.isArray(data.file_locations)) {
            const formattedFiles = data.file_locations.map((file: any): FileDetails => ({
              referenceId: file.reference_code ?? '-',
              txtFileLink: file.processed_file_s3_location ?? '-',
              status: file.job_status ?? '-',
            }));

            setFileHistory(formattedFiles);
          } else {
            console.warn('Expected file_locations to be an array, but received:', data.file_locations);
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

      const formData = new FormData();
      formData.append('file', file);
      formData.append('email', email);

      const response = await fetch(
        `https://q3mhoyo8i9.execute-api.us-east-1.amazonaws.com/prod/nerprocess`,
        {
          method: 'POST',
          body: formData,
        }
      );

      if (response.ok) {
        const updatedFile = await response.json();

        const formattedFile: FileDetails = {
          referenceId: updatedFile.reference_code ?? '-',
          txtFileLink: updatedFile.processed_file_s3_location ?? '-',
          status: updatedFile.job_status ?? '-',
        };

        if (updatedFile.job_status === 'SUCCEEDED') {
          await sendEmailToUser(updatedFile);
        }

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

  const sendEmailToUser = async (fileDetails: any) => {
    try {
      const emailPayload = {
        email: user?.email,
        subject: 'File Processing Completed',
        message: `Your file has been successfully processed. Details:

        Reference Code: ${fileDetails.reference_code}
        Processed File: ${fileDetails.processed_file_s3_location}
        Status: ${fileDetails.job_status}`,
      };

      const emailResponse = await fetch(
        `https://4felas5im2setdsipr3dryjdhi0jeauv.lambda-url.us-east-1.on.aws/`,
        {
          method: 'POST',
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(emailPayload),
        }
      );

      if (emailResponse.ok) {
        console.log('Email sent successfully');
      } else {
        console.error('Error sending email:', await emailResponse.text());
      }
    } catch (error) {
      console.error('Error:', error);
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